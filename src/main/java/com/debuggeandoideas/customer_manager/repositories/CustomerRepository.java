package com.debuggeandoideas.customer_manager.repositories;

import com.debuggeandoideas.customer_manager.tables.CustomerTable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface CustomerRepository extends R2dbcRepository<CustomerTable, Long> {

    @Query("SELECT * FROM customer WHERE email = :email")
    Mono<CustomerTable> findByEmail(String email);

    @Query("SELECT COUNT(*) > 0 FROM customer WHERE email=:email")
    Mono<Boolean> existByEmail(String email);
}
