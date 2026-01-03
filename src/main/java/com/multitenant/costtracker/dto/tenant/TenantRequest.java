package com.multitenant.costtracker.dto.tenant;


import jakarta.validation.constraints.NotBlank;

public class TenantRequest {
    @NotBlank
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
