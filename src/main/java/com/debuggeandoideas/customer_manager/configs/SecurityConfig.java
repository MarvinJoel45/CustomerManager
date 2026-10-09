package com.debuggeandoideas.customer_manager.configs;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final ReactiveAuthenticationManager authenticationManager;
    private final ServerAuthenticationConverter converter;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(authenticationManager);
        authenticationWebFilter.setServerAuthenticationConverter(converter);

        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchange -> exchange
                        .pathMatchers(HttpMethod.POST,LOGIN_ENDPOINT).permitAll()
                        .pathMatchers(HttpMethod.POST,REGISTER_ENDPOINT).permitAll()

                        .pathMatchers(MANAGER_PATH).hasRole(ROLE_ADMIN)
                        .pathMatchers(RATING_PATH).hasRole(ROLE_PREMIUM)
                        .pathMatchers(RESERVATION_PATH).hasRole(ROLE_FREE)

                        .anyExchange().authenticated()
                )
                .addFilterAt(authenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private static final String LOGIN_ENDPOINT = "/customer/auth/login";
    private static final String REGISTER_ENDPOINT = "/customer/auth/register";
    private static final String MANAGER_PATH = "/customer/manager/**";
    private static final String RATING_PATH = "/customer/rating/**";
    private static final String RESERVATION_PATH = "/customer/reservation/**";

    private static final String ROLE_ADMIN = "admin_user";
    private static final String ROLE_PREMIUM = "premium_user";
    private static final String ROLE_FREE = "free_user";
}
