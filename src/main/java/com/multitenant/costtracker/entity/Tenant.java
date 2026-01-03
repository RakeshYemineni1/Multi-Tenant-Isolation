package com.multitenant.costtracker.entity;


import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "tenants", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class Tenant {

    @Id
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    public Tenant() {
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
