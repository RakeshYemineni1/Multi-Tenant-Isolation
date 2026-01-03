package com.multitenant.costtracker.repository;

import com.multitenant.costtracker.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TeamRepository extends JpaRepository<Team, UUID> {

    List<Team> findByTenantId(UUID tenantId);
    Optional<Team> findByIdAndTenantId(UUID id, UUID tenantId);

}
