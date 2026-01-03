package com.multitenant.costtracker.dto.report;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class TeamBreakdownReportResponse {
    private UUID tenantId;
    private LocalDate from;
    private LocalDate to;
    private List<TeamCostItem> items;

    public TeamBreakdownReportResponse(UUID tenantId, LocalDate from, LocalDate to, List<TeamCostItem> items) {
        this.tenantId = tenantId;
        this.from = from;
        this.to = to;
        this.items = items;
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

    public List<TeamCostItem> getItems() {
        return items;
    }

    public static class TeamCostItem {
        private UUID teamId;
        private String teamName;
        private BigDecimal totalCost;

        public TeamCostItem(UUID teamId, String teamName, BigDecimal totalCost) {
            this.teamId = teamId;
            this.teamName = teamName;
            this.totalCost = totalCost;
        }

        public UUID getTeamId() {
            return teamId;
        }

        public String getTeamName() {
            return teamName;
        }

        public BigDecimal getTotalCost() {
            return totalCost;
        }
    }
}