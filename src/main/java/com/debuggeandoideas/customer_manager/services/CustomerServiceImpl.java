package com.debuggeandoideas.customer_manager.services;

import com.debuggeandoideas.customer_manager.enums.UpdateRoleOperation;
import com.debuggeandoideas.customer_manager.repositories.CustomerRepository;
import com.debuggeandoideas.customer_manager.repositories.RoleRepository;
import com.debuggeandoideas.customer_manager.tables.CustomerTable;
import com.debuggeandoideas.customer_manager.tables.RoleTable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final RoleRepository roleRepository;
    private final DatabaseClient databaseClient;

    @Override
    public Mono<CustomerTable> createCustomer(CustomerTable customerTable, Set<RoleTable> roleNames) {
        log.info("Create customer with mail:{}",customerTable.getEmail());

        return this.customerRepository.save(customerTable)
                .flatMap(savedCustomer -> {
                    return Flux.fromIterable(roleNames)
                            .flatMap(roleName ->
                                    this.databaseClient.sql(INSERT_BY_ROLE_QUERY)
                                            .bind("customerId",savedCustomer.getId())
                                            .bind("roleName",roleName)
                                            .fetch()
                                            .rowsUpdated()
                            )
                            .then(Mono.just(savedCustomer));
                })
                .doOnSuccess(createdCustomer -> log.info("Customer created:{}",createdCustomer.getId()))
                .doOnError(error -> log.error("Error creating customer",error));
    }

    @Override
    public Mono<Map<String, List<RoleTable>>> readRolesByEmail(String email) {
        log.info("Reading role by email:{}",email);

        return this.customerRepository.findByEmail(email)
                .flatMap(customer ->
                        this.databaseClient
                            .sql(SELECT_BY_ROLE_QUERY)
                            .bind("customerId",customer.getId())
                            .map((row,metadata) -> {
                                RoleTable roleTable = new RoleTable();
                                roleTable.setName(row.get("name", String.class));
                                roleTable.setDescription(row.get("description", String.class));
                                return roleTable;
                            })
                            .all()
                            .collectList()
                            .map(roles -> Map.of(email,roles))
                )
                .switchIfEmpty(Mono.just(Map.of(email,List.of())));
    }

    @Override
    public Mono<Void> deleteCustomer(Long id) {
        log.info("Deleting customer with id:{}",id);

        return this.customerRepository.deleteById(id);
    }

    @Override
    public Mono<CustomerTable> updateRoleInCustomer(Long id, Set<String> roleName, UpdateRoleOperation operation) {
        return null;
    }

    private static final String INSERT_BY_ROLE_QUERY = """
            INSERT INTO customer_role (customer_id,role_name)
            VALUES (:customerId,:roleName)
            """;

    private static final String SELECT_BY_ROLE_QUERY = """
            SELECT r.name, r.description
            FROM role r
            INNER JOIN customer_role cr ON cr.role_name = r.name
            WHERE cr.customer_id = :customerId""";

}
