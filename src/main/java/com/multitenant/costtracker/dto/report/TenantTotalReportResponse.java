package com.multitenant.costtracker.dto.report;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class TenantTotalReportResponse {
    private UUID tenantId;
    private LocalDate from;
    private LocalDate to;
    private BigDecimal totalCost;

    public TenantTotalReportResponse(UUID tenantId, LocalDate from, LocalDate to, BigDecimal totalCost) {
        this.tenantId = tenantId;
        this.from = from;
        this.to = to;
        this.totalCost = totalCost;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public LocalDate getFrom() {
        return from;
    }

    public LocalDate getTo() {
        return to;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }
}