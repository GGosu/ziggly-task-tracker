package com.ziggly.teamservice.service;

import com.ziggly.model.dto.TeamUserDTO;
import com.ziggly.model.team.Team;
import com.ziggly.model.team.TeamUser;
import com.ziggly.model.utils.Utils;
import com.ziggly.teamservice.api.TeamUserService;
import com.ziggly.teamservice.repository.TeamRepository;
import com.ziggly.teamservice.repository.TeamUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TeamUserServiceImpl implements TeamUserService {
    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TeamUserRepository teamUserRepository;
    @Override
    public ResponseEntity<String> addUserToTeam(Integer teamId, TeamUserDTO teamUserDTO, HttpServletRequest request) {

        Integer userId = Utils.getUserId(request);
        Optional<Team> team = teamRepository.findById(teamId);

        if(team.isPresent() && team.get().getOwnerId().equals(userId)) {

            if (teamUserRepository.findByUserIdAndTeamId(teamUserDTO.getUserId(), teamId).isPresent()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("User already assigned to the team.");
            }

            TeamUser teamUser = new TeamUser();
            teamUser.setUserId(teamUserDTO.getUserId());
            teamUser.setTeam(team.get());
            teamUser.setRole(teamUserDTO.getRole());
            teamUser.setJoinedAt(System.currentTimeMillis());

            teamUserRepository.save(teamUser);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body("User added to team successfully!");
        }
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("Team not found or unauthorized action!");

    }

    @Override
    public ResponseEntity<String> updateUserRoleInTeam(Integer teamId, Integer userId, TeamUserDTO teamUserDTO, HttpServletRequest request) {
        Integer requestUserId = Utils.getUserId(request);
        Optional<Team> team = teamRepository.findById(teamId);

        if (team.isPresent() && team.get().getOwnerId().equals(requestUserId)) {
            Optional<TeamUser> teamUser = teamUserRepository.findByUserIdAndTeamId(userId, teamId);
            if (teamUser.isPresent()) {
                teamUser.get().setRole(teamUserDTO.getRole());
                teamUserRepository.save(teamUser.get());

                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body("User role updated successfully!");
            }
        }
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("Team or user not found!");
    }

    @Override
    public ResponseEntity<String> removeUserFromTeam(Integer teamId, Integer userId, HttpServletRequest request) {
        Integer requestUserId = Utils.getUserId(request);
        Optional<Team> team = teamRepository.findById(teamId);

        if (team.isPresent() && team.get().getOwnerId().equals(requestUserId)) {
            Optional<TeamUser> teamUser = teamUserRepository.findByUserIdAndTeamId(userId, teamId);

            if (teamUser.isPresent()) {
                teamUserRepository.delete(teamUser.get());

                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body("User removed successfully!");
            }

        }
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("Team or user not found!");
    }

    @Override
    public ResponseEntity<List<TeamUserDTO>> getUsersInTeam(Integer teamId, HttpServletRequest request) {
        Optional<Team> team = teamRepository.findById(teamId);

        if (team.isPresent()) {

            List<TeamUser> teamUsers = teamUserRepository.findByTeamId(teamId);
            List<TeamUserDTO> teamUserDTOs = teamUsers.stream()
                    .map(tu -> new TeamUserDTO(tu.getId(), tu.getUserId(), tu.getTeam().getId(), tu.getRole()))
                    .collect(Collectors.toList());

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(teamUserDTOs);
        }
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(null);
    }
}
