package com.multitenant.costtracker.dto.team;

import java.util.UUID;

public class TeamResponse {

    private UUID id;
    private String name;

    public TeamResponse(UUID id, String name) {
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
