package com.debuggeandoideas.customer_manager.controllers;

import com.debuggeandoideas.customer_manager.dtos.LoginRequest;
import com.debuggeandoideas.customer_manager.dtos.LoginResponse;
import com.debuggeandoideas.customer_manager.security.AuthService;
import com.debuggeandoideas.customer_manager.services.CustomerService;
import com.debuggeandoideas.customer_manager.tables.CustomerTable;
import com.debuggeandoideas.customer_manager.tables.RoleTable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "auth")
public class AuthController {

    private final CustomerService customerService;
    private final AuthService authService;
    private final BCryptPasswordEncoder passwordEncoder;

    @PostMapping(path = "login")
    public Mono<ResponseEntity<LoginResponse>> login(@RequestBody LoginRequest loginRequest) {

        return this.authService.authenticate(loginRequest.getEmail(),loginRequest.getPassword())
                .flatMap(jwt ->
                    this.customerService.readRolesByEmail(loginRequest.getEmail())
                            .map(rolesMap -> {
                                List<String> rolesNames = rolesMap.values().stream()
                                        .flatMap(List::stream)
                                        .map(RoleTable::getName)
                                        .toList();

                                LoginResponse loginResponse = new LoginResponse(
                                        jwt,loginRequest.getEmail(),rolesNames);
                                return ResponseEntity.ok(loginResponse);
                            })
                )
                .onErrorResume(error -> {
                    log.error(error.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
                });
    }

    @PostMapping(path = "register")
    public Mono<ResponseEntity<CustomerTable>> createCustomer(@RequestBody CustomerTable customer,
                                                              @RequestParam Set<String> roles) {
        log.info("POST auth/register");

        customer.setPassword(passwordEncoder.encode(customer.getPassword()));

        return customerService.createCustomer(customer, roles)
                .map(createdCustomer -> ResponseEntity
                        .created(URI.create("auth/register" + createdCustomer.getId()))
                        .body(createdCustomer))
                .onErrorResume(IllegalArgumentException.class, error -> {
                    log.error("POST auth/register failed", error);
                    return Mono.just(ResponseEntity.badRequest().build());
                })
                .onErrorResume(RuntimeException.class, error -> {
                    log.error("POST auth/register failed", error);
                    return Mono.just(ResponseEntity.internalServerError().build());
                });
    }

}
