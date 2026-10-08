package com.debuggeandoideas.customer_manager.controllers;

import com.debuggeandoideas.customer_manager.clients.ReservationCrudClient;
import com.debuggeandoideas.customer_manager.dtos.ReservationRequest;
import com.debuggeandoideas.customer_manager.dtos.ReservationResponse;
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

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "reservation")
public class ReservationController {

    private final ReservationCrudClient reservationCrudClient;

    @PostMapping
    public Mono<ResponseEntity<ReservationResponse>> postReservation(@RequestBody ReservationRequest request) {
        log.info("POST customer/reservation");
        return null;
    }

    @GetMapping(path = "{id}")
    public Mono<ResponseEntity<ReservationResponse>> getReservation(@PathVariable String id) {
        log.info("GET customer/reservation/{}",id);

        return null;
    }

    @PutMapping(path = "{id}")
    public Mono<ResponseEntity<ReservationResponse>> putReservation(@PathVariable String id,
                                                                    @RequestBody ReservationRequest request) {
        log.info("PUT customer/reservation/{}",id);

        return null;
    }

    @DeleteMapping(path = "{id}")
    public Mono<ResponseEntity<Void>> deleteReservation(@PathVariable String id) {
        log.info("DELETE customer/reservation/{}",id);

        return null;
    }
}
