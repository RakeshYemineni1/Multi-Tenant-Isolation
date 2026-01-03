package com.multitenant.costtracker.controller;

import com.multitenant.costtracker.dto.costRecord.CostRecordRequest;
import com.multitenant.costtracker.dto.costRecord.CostRecordResponse;
import com.multitenant.costtracker.entity.CostRecord;
import com.multitenant.costtracker.service.CostService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/cost-records")
public class CostRecordController {

    private final CostService costService;

    public CostRecordController(CostService costService){
        this.costService = costService;
    }

    @PostMapping
    public CostRecordResponse createRecord(@Valid @RequestBody CostRecordRequest costRecordRequest){
        CostRecord costRecord = costService.createRecord(costRecordRequest.getTeamId(), costRecordRequest.getCostAmount(), costRecordRequest.getDate());
        return new CostRecordResponse(costRecord.getId(), costRecord.getTeamId(), costRecord.getDate(), costRecord.getCost());
    }

    @GetMapping
    public List<CostRecordResponse> getCostRecords(@RequestParam LocalDate from, @RequestParam LocalDate to){
        return costService.getCostRecords(from, to).stream()
                .map(record -> new CostRecordResponse(record.getId(), record.getTeamId(), record.getDate(), record.getCost()))
                .toList();
    }
}
