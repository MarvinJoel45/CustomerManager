package com.debuggeandoideas.customer_manager.services;

import com.debuggeandoideas.customer_manager.enums.UpdateRoleOperation;
import com.debuggeandoideas.customer_manager.tables.CustomerTable;
import com.debuggeandoideas.customer_manager.tables.RoleTable;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface CustomerService {

    Mono<CustomerTable> createCustomer(CustomerTable customerTable, Set<RoleTable> roleNames);
    Mono<Map<String, List<RoleTable>>> readRolesByEmail(String email);
    Mono<Void> deleteCustomer(Long id);
    Mono<CustomerTable> updateRoleInCustomer(Long id, Set<String> roleNames, UpdateRoleOperation operation);
}
