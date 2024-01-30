package com.ziggly.teamservice.api;

import com.ziggly.model.dto.TeamUserDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface TeamUserService {

    @PostMapping("/api/team/{teamId}/users")
    ResponseEntity<String> addUserToTeam(@PathVariable Integer teamId, @RequestBody TeamUserDTO teamUserDTO, HttpServletRequest request);
    @PutMapping("/api/team/{teamId}/users/{userId}")
    ResponseEntity<String> updateUserRoleInTeam(@PathVariable Integer teamId, @PathVariable Integer userId, @RequestBody TeamUserDTO teamUserDTO, HttpServletRequest request);
    @DeleteMapping("/api/team/{teamId}/users/{userId}")
    ResponseEntity<String> removeUserFromTeam(@PathVariable Integer teamId, @PathVariable Integer userId, HttpServletRequest request);
    @GetMapping("/api/team/{teamId}/users")
    ResponseEntity<List<TeamUserDTO>> getUsersInTeam(@PathVariable Integer teamId, HttpServletRequest request);

}
