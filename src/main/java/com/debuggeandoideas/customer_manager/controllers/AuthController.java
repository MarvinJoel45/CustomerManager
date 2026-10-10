package com.debuggeandoideas.customer_manager.controllers;

import com.debuggeandoideas.customer_manager.dtos.LoginRequest;
import com.debuggeandoideas.customer_manager.dtos.LoginResponse;
import com.debuggeandoideas.customer_manager.security.AuthService;
import com.debuggeandoideas.customer_manager.services.CustomerService;
import com.debuggeandoideas.customer_manager.tables.RoleTable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "auth")
public class AuthController {

    private final CustomerService customerService;
    private final AuthService authService;

    public Mono<ResponseEntity<LoginResponse>> login(LoginRequest loginRequest) {

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

}
