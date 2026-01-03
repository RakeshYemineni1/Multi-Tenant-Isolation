package com.multitenant.costtracker.dto.report;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class ReportResponse {

    private UUID teamId;
    private LocalDate from;
    private LocalDate to;
    private BigDecimal totalCost;

    public ReportResponse(UUID teamId, LocalDate from, LocalDate to, BigDecimal totalCost) {
        this.teamId = teamId;
        this.from = from;
        this.to = to;
        this.totalCost = totalCost;
    }

    public UUID getTeamId() {
        return teamId;
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
