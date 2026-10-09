package com.debuggeandoideas.customer_manager.repositories;

import com.debuggeandoideas.customer_manager.tables.RoleTable;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface RoleRepository extends R2dbcRepository<RoleTable, String> {
}
