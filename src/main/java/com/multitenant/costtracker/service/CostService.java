package com.multitenant.costtracker.service;

import com.multitenant.costtracker.context.TenantContext;
import com.multitenant.costtracker.entity.CostRecord;
import com.multitenant.costtracker.repository.CostRecordRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class CostService {

    private final CostRecordRepository costRecordRepo;

    public CostService(CostRecordRepository costRecordRepo){
        this.costRecordRepo = costRecordRepo;
    }

    public CostRecord createRecord(UUID teamId, BigDecimal cost, LocalDate date){
        UUID tenantId = TenantContext.get();

        if(tenantId == null){
            throw new RuntimeException("Missing X-Tenant-Id");
        }

        CostRecord record = new CostRecord();
        record.setId(UUID.randomUUID());
        record.setTenantId(tenantId);
        record.setTeamId(teamId);
        record.setCost(cost);
        record.setDate(date);
        return costRecordRepo.save(record);
    }

    public List<CostRecord> getCostRecords(LocalDate from, LocalDate to){
        UUID tenantId = TenantContext.get();
        if(tenantId == null){
            throw new RuntimeException("Missing X-Tenant-Id");
        }
        return costRecordRepo.findByTenantIdAndDateBetween(tenantId, from, to);
    }
}
