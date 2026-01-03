package com.multitenant.costtracker.service;

import com.multitenant.costtracker.context.TenantContext;
import com.multitenant.costtracker.dto.report.TenantTotalReportResponse;
import com.multitenant.costtracker.dto.report.TeamBreakdownReportResponse;
import com.multitenant.costtracker.entity.CostRecord;
import com.multitenant.costtracker.entity.Team;
import com.multitenant.costtracker.repository.CostRecordRepository;
import com.multitenant.costtracker.repository.TeamRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ReportService {

    private final CostRecordRepository costRecordRepo;
    private final TeamRepository teamRepo;

    public ReportService(CostRecordRepository costRecordRepo, TeamRepository teamRepo){
        this.costRecordRepo = costRecordRepo;
        this.teamRepo = teamRepo;
    }

    public TenantTotalReportResponse getTenantTotalReport(LocalDate from, LocalDate to){
        UUID tenantId = TenantContext.get();
        if(tenantId == null){
            throw new RuntimeException("Missing X-Tenant-Id");
        }
        BigDecimal total = costRecordRepo.totalCostForTenant(tenantId, from, to);
        return new TenantTotalReportResponse(tenantId, from, to, total);
    }

    public TeamBreakdownReportResponse getTeamBreakdownReport(LocalDate from, LocalDate to){
        UUID tenantId = TenantContext.get();
        if(tenantId == null){
            throw new RuntimeException("Missing X-Tenant-Id");
        }
        
        List<CostRecord> records = costRecordRepo.findByTenantIdAndDateBetween(tenantId, from, to);
        List<Team> teams = teamRepo.findByTenantId(tenantId);
        
        Map<UUID, String> teamNames = teams.stream()
                .collect(Collectors.toMap(Team::getId, Team::getName));
        
        Map<UUID, BigDecimal> teamCosts = records.stream()
                .collect(Collectors.groupingBy(
                        CostRecord::getTeamId,
                        Collectors.reducing(BigDecimal.ZERO, CostRecord::getCost, BigDecimal::add)
                ));
        
        List<TeamBreakdownReportResponse.TeamCostItem> items = teamCosts.entrySet().stream()
                .map(entry -> new TeamBreakdownReportResponse.TeamCostItem(
                        entry.getKey(),
                        teamNames.getOrDefault(entry.getKey(), "Unknown Team"),
                        entry.getValue()
                ))
                .toList();
        
        return new TeamBreakdownReportResponse(tenantId, from, to, items);
    }
}
