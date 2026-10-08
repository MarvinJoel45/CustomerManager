package com.debuggeandoideas.customer_manager.controllers;

import com.debuggeandoideas.customer_manager.clients.ReservationCrudClient;
import com.debuggeandoideas.customer_manager.dtos.ReservationRequest;
import com.debuggeandoideas.customer_manager.dtos.ReservationResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "reservation")
public class ReservationController {

    private final ReservationCrudClient reservationCrudClient;

    @PostMapping
    public Mono<ResponseEntity<Object>> postReservation(@Valid @RequestBody ReservationRequest request) {
        log.info("POST customer/reservation");

        return this.reservationCrudClient.create(request)
                .map(resource -> ResponseEntity.created(URI.create(resource)).build())
                .onErrorResume(IllegalArgumentException.class, error -> {
                    log.error("POST customer/reservation failed");
                    return Mono.just(ResponseEntity.badRequest().build());
                })
                .onErrorResume(RuntimeException.class, error -> {
                    log.error("POST customer/reservation failed");
                    return Mono.just(ResponseEntity.internalServerError().build());
                });
    }

    @GetMapping(path = "{id}")
    public Mono<ResponseEntity<ReservationResponse>> getReservation(@PathVariable String id) {
        log.info("GET customer/reservation/{}",id);

        return this.reservationCrudClient.read(id)
                .map(ResponseEntity::ok)
                .onErrorResume(IllegalArgumentException.class, error -> {
                    log.error("GET customer/reservation failed");
                    return Mono.just(ResponseEntity.badRequest().build());
                })
                .onErrorResume(RuntimeException.class, error -> {
                    log.error("GET customer/reservation failed");
                    return Mono.just(ResponseEntity.internalServerError().build());
                });
    }

    @PutMapping(path = "{id}")
    public Mono<ResponseEntity<ReservationResponse>> putReservation(@PathVariable String id,
                                                                    @Valid @RequestBody ReservationRequest request) {
        log.info("PUT customer/reservation/{}",id);

        return this.reservationCrudClient.update(id,request)
                .map(ResponseEntity::ok)
                .onErrorResume(IllegalArgumentException.class, error -> {
                    log.error("PUT customer/reservation failed");
                    return Mono.just(ResponseEntity.badRequest().build());
                })
                .onErrorResume(RuntimeException.class, error -> {
                    log.error("PUT customer/reservation failed");
                    return Mono.just(ResponseEntity.internalServerError().build());
                });
    }

    @DeleteMapping(path = "{id}")
    public Mono<ResponseEntity<Object>> deleteReservation(@PathVariable String id) {
        log.info("DELETE customer/reservation/{}",id);

        return this.reservationCrudClient.delete(id)
                .then(Mono.just(ResponseEntity.noContent().build()))
                .onErrorResume(IllegalArgumentException.class, error -> {
                    log.error("DELETE customer/reservation failed");
                    return Mono.just(ResponseEntity.badRequest().build());
                })
                .onErrorResume(RuntimeException.class, error -> {
                    log.error("DELETE customer/reservation failed");
                    return Mono.just(ResponseEntity.internalServerError().build());
                });
    }
}
