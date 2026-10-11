package com.debuggeandoideas.customer_manager.streams;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.function.Supplier;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class RatingStreamConfig {

    private final ObjectMapper objectMapper;
    private static final String CREATED_RATING_EVENT_NAME = "RATING_CREATED";

    @Bean
    public Sinks.Many<Message<String>> ratingRequestSink() {
        log.info("Creating rating sink");
        return Sinks.many().multicast().onBackpressureBuffer(1_000);
    }

    /**
     * This supplier is to send msg to kafka(procedure)
     * @param sink
     * @return
     */
    @Bean
    public Supplier<Flux<Message<String>>> ratingRequestSupplier(Sinks.Many<Message<String>> sink) {
        return () -> sink.asFlux()
                .doOnSubscribe(sub -> log.info("Subscribing to rating sink"))
                .doOnCancel(() -> log.info("Canceling rating sink"));
    }
}
