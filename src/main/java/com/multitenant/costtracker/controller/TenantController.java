package com.multitenant.costtracker.controller;

import com.multitenant.costtracker.dto.tenant.TenantRequest;
import com.multitenant.costtracker.dto.tenant.TenantResponse;
import com.multitenant.costtracker.entity.Tenant;
import com.multitenant.costtracker.service.TenantService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tenants")
public class TenantController {

    private final TenantService tenantService;

    public TenantController(TenantService tenantService){
        this.tenantService = tenantService;
    }

    @PostMapping
    public TenantResponse createTenant(@Valid @RequestBody TenantRequest tenantRequest){
        Tenant tenant = tenantService.create(tenantRequest.getName());
        return new TenantResponse(tenant.getId(), tenant.getName());
    }
}
