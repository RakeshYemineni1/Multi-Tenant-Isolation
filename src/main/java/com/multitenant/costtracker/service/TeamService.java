package com.multitenant.costtracker.service;

import com.multitenant.costtracker.context.TenantContext;
import com.multitenant.costtracker.entity.Team;
import com.multitenant.costtracker.repository.TeamRepository;
import org.hibernate.sql.Template;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TeamService {

    private final TeamRepository teamRepo;

    public TeamService(TeamRepository teamRepo){
        this.teamRepo = teamRepo;
    }

    public Team create(String name){
        UUID tenantId = TenantContext.get();
        if(tenantId == null){
            throw new RuntimeException("Missing X-Tenant-Id");
        }
        Team team = new Team();
        team.setId(UUID.randomUUID());
        team.setTenantId(tenantId);
        team.setName(name);
        return teamRepo.save(team);
    }

    public List<Team> teamsList(){
        UUID tenantId = TenantContext.get();
        if(tenantId == null){
            throw new RuntimeException("Missing X-Tenant-Id");
        }
        return teamRepo.findByTenantId(tenantId);
    }
}
