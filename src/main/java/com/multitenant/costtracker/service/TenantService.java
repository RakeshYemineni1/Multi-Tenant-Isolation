package com.multitenant.costtracker.service;

import com.multitenant.costtracker.entity.Tenant;
import com.multitenant.costtracker.repository.TenantRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TenantService {

    private TenantRepository tenantRepo;

    public TenantService(TenantRepository tenantRepo){
        this.tenantRepo = tenantRepo;
    }

    public Tenant create(String name){
        if(tenantRepo.existsByName(name)){
           throw new IllegalStateException("Tenant Already Exists");
        }
        Tenant tenant = new Tenant();
        tenant.setId(UUID.randomUUID());
        tenant.setName(name);
        return tenantRepo.save(tenant);
    }
}
