package com.multitenant.costtracker.controller;

import com.multitenant.costtracker.dto.costRecord.CostRecordResponse;
import com.multitenant.costtracker.dto.report.ReportResponse;
import com.multitenant.costtracker.dto.report.TenantTotalReportResponse;
import com.multitenant.costtracker.dto.report.TeamBreakdownReportResponse;
import com.multitenant.costtracker.service.ReportService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService){
        this.reportService = reportService;
    }

    @GetMapping("/total")
    public TenantTotalReportResponse getTotalReport(@RequestParam LocalDate from, @RequestParam LocalDate to){
        return reportService.getTenantTotalReport(from, to);
    }

    @GetMapping("/by-team")
    public TeamBreakdownReportResponse getTeamBreakdownReport(@RequestParam LocalDate from, @RequestParam LocalDate to){
        return reportService.getTeamBreakdownReport(from, to);
    }
}
