package com.multitenant.costtracker.repository;

import com.multitenant.costtracker.entity.CostRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface CostRecordRepository extends JpaRepository<CostRecord, UUID> {

    List<CostRecord> findByTenantIdAndDateBetween(
            UUID tenantId,
            LocalDate from,
            LocalDate to
    );

    @Query("""
        SELECT COALESCE(SUM(c.cost), 0)
        FROM CostRecord c
        WHERE c.tenantId = :tenantId
          AND c.date BETWEEN :from AND :to
    """)
    BigDecimal totalCostForTenant(
            @Param("tenantId") UUID tenantId,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to
    );
}
