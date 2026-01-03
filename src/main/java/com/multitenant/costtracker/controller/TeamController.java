package com.multitenant.costtracker.controller;

import com.multitenant.costtracker.dto.team.TeamRequest;
import com.multitenant.costtracker.dto.team.TeamResponse;
import com.multitenant.costtracker.entity.Team;
import com.multitenant.costtracker.service.TeamService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teams")
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService){
        this.teamService = teamService;
    }

    @PostMapping
    public TeamResponse createTeam(@Valid @RequestBody TeamRequest teamRequest){
        Team team = teamService.create(teamRequest.getName());
        return new TeamResponse(team.getId(), team.getName());
    }

    @GetMapping
    public List<TeamResponse> list(){
        return teamService.teamsList().stream().map(team -> new TeamResponse(team.getId(), team.getName())).toList();
    }
}
