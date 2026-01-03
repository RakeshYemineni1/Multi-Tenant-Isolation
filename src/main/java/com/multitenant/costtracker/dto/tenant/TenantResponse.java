package com.multitenant.costtracker.dto.tenant;

import java.util.UUID;

public class TenantResponse {

    private UUID id;
    private String name;

    public TenantResponse(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
