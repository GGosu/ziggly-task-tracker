package com.ziggly.teamservice.controller;

import com.ziggly.model.dto.TeamDTO;
import com.ziggly.teamservice.api.TeamService;
import com.ziggly.teamservice.service.TeamServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TeamController implements TeamService {
    @Autowired
    private TeamServiceImpl teamsService;

    @Override
    public ResponseEntity<String> createTeam(TeamDTO teamDTO, HttpServletRequest request) {
        return teamsService.createTeam(teamDTO, request);
    }

    @Override
    public ResponseEntity<TeamDTO> getTeam(Integer teamId, HttpServletRequest request) {
        return teamsService.getTeam(teamId, request);
    }

    @Override
    public ResponseEntity<String> updateTeam(Integer teamId, TeamDTO teamDTO, HttpServletRequest request) {
        return teamsService.updateTeam(teamId, teamDTO, request);
    }

    @Override
    public ResponseEntity<String> deleteTeam(Integer teamId, HttpServletRequest request) {
        return teamsService.deleteTeam(teamId, request);
    }
}
