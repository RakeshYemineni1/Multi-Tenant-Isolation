package com.multitenant.costtracker.repository;

import com.multitenant.costtracker.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TenantRepository extends JpaRepository<Tenant, UUID> {
    boolean existsByName(String name);
}
