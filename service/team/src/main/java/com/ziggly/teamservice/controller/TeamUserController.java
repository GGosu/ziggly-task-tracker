package com.ziggly.teamservice.controller;

import com.ziggly.model.dto.TeamUserDTO;
import com.ziggly.teamservice.api.TeamUserService;
import com.ziggly.teamservice.service.TeamUserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TeamUserController implements TeamUserService {
    @Autowired
    TeamUserServiceImpl teamsUserService;
    @Override
    public ResponseEntity<String> addUserToTeam(Integer teamId, TeamUserDTO teamUserDTO, HttpServletRequest request) {
        return teamsUserService.addUserToTeam(teamId, teamUserDTO, request);
    }

    @Override
    public ResponseEntity<String> updateUserRoleInTeam(Integer teamId, Integer userId, TeamUserDTO teamUserDTO, HttpServletRequest request) {
        return teamsUserService.updateUserRoleInTeam(teamId, userId, teamUserDTO, request);
    }

    @Override
    public ResponseEntity<String> removeUserFromTeam(Integer teamId, Integer userId, HttpServletRequest request) {
        return teamsUserService.removeUserFromTeam(teamId, userId, request);
    }

    @Override
    public ResponseEntity<List<TeamUserDTO>> getUsersInTeam(Integer teamId, HttpServletRequest request) {
        return teamsUserService.getUsersInTeam(teamId, request);
    }
}
