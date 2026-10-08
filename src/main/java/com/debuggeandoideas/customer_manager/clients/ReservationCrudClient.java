package com.debuggeandoideas.customer_manager.clients;

import com.debuggeandoideas.customer_manager.dtos.ReservationRequest;
import com.debuggeandoideas.customer_manager.dtos.ReservationResponse;
import reactor.core.publisher.Mono;

public interface ReservationCrudClient {

    Mono<String> create(ReservationRequest reservationRequest);
    Mono<ReservationResponse> read(String uuid);
    Mono<ReservationResponse> update(String uuid, ReservationRequest reservationRequest);
    Mono<Void> delete(String uuid);
}
