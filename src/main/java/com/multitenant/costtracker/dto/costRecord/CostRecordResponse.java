package com.multitenant.costtracker.dto.costRecord;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class CostRecordResponse {

    private UUID id;
    private UUID teamId;
    private LocalDate date;
    private BigDecimal cost;

    public CostRecordResponse(UUID id, UUID teamId, LocalDate date, BigDecimal cost) {
        this.id = id;
        this.teamId = teamId;
        this.date = date;
        this.cost = cost;
    }

    public UUID getId() {
        return id;
    }

    public UUID getTeamId() {
        return teamId;
    }

    public LocalDate getDate() {
        return date;
    }

    public BigDecimal getCost() {
        return cost;
    }
}
