package com.ziggly.teamservice.api;

import com.ziggly.model.dto.TeamDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@Service
public interface TeamService {
    @PostMapping("/api/team")
    ResponseEntity<String> createTeam(@RequestBody TeamDTO teamDTO, HttpServletRequest request);

    @GetMapping("/api/team/{teamId}")
    ResponseEntity<TeamDTO> getTeam(@PathVariable Integer teamId, HttpServletRequest request);

    @PutMapping("/api/team/{teamId}")
    ResponseEntity<String> updateTeam(@PathVariable Integer teamId, @RequestBody TeamDTO teamDTO, HttpServletRequest request);

    @DeleteMapping("/api/team/{teamId}")
    ResponseEntity<String> deleteTeam(@PathVariable Integer teamId, HttpServletRequest request);

}
