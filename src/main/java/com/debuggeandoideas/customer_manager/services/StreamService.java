package com.debuggeandoideas.customer_manager.services;

import com.debuggeandoideas.customer_manager.dtos.RatingRequest;
import com.debuggeandoideas.customer_manager.exceptions.RetryableStreamException;
import com.debuggeandoideas.customer_manager.exceptions.StreamTerminateException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.util.retry.Retry;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class StreamService {

    private final Sinks.Many<Message<String>> ratingRequestSink;
    private final ObjectMapper objectMapper;

    public Mono<Object> sendRatingToStream(RatingRequest request) {
        return Mono.fromCallable(() -> this.objectMapper.writeValueAsString(request))
                .flatMap(json -> {
                    Message<String> message = MessageBuilder.withPayload(json)
                            .setHeader(KafkaHeaders.KEY, request.getIdRestaurant().toString().getBytes()) //Kafka trabaja con bytes
                            .setHeader("contentType","text/plain")
                            .build();

                    Sinks.EmitResult result = this.ratingRequestSink.tryEmitNext(message);

                    if (result.isSuccess()) {
                        log.info("Rating request sent successfully");
                        return Mono.empty();
                    }

                    return Mono.error(this.mapEmitResult(result));
                })
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(1))
                        .filter(ex -> ex instanceof RetryableStreamException)
                        .onRetryExhaustedThrow((spec,sig) -> sig.failure())
                )
                .doOnError(err -> log.error(err.getMessage(),err));
    }

    /**
     * Metodo para emitir los errores
     */
    private RuntimeException mapEmitResult(Sinks.EmitResult result) {
        return switch (result) {
            case FAIL_OVERFLOW, FAIL_NON_SERIALIZED ->
                new RetryableStreamException("Transient sink");
            case FAIL_CANCELLED,FAIL_TERMINATED ->
                    new StreamTerminateException("Sink terminated");
            default -> new RuntimeException("Unexpected sink result");
        };
    }
}
