package com.ziggly.teamservice.service;

import com.ziggly.model.dto.TeamDTO;
import com.ziggly.model.team.Team;
import com.ziggly.model.utils.Utils;
import com.ziggly.teamservice.api.TeamService;
import com.ziggly.teamservice.repository.TeamRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.Optional;



@Service
public class TeamServiceImpl implements TeamService {
    @Autowired
    private TeamRepository teamRepository;

    private boolean isValidNameDescriptionTeamDTO(TeamDTO teamDTO) {
        return teamDTO != null &&
                StringUtils.hasText(teamDTO.getName()) &&
                teamDTO.getName().length() <= 255 &&
                (teamDTO.getDescription() == null || teamDTO.getDescription().length() <= 1000);
    }

    @Override
    public ResponseEntity<String> createTeam(TeamDTO teamDTO, HttpServletRequest request) {

        if(isValidNameDescriptionTeamDTO(teamDTO)){

            Integer userId = Utils.getUserId(request);
            if(userId != null ){

                Optional<Team> existingTeam = teamRepository.findByName(teamDTO.getName());
                if (existingTeam.isPresent()) {
                    return ResponseEntity
                            .status(HttpStatus.BAD_REQUEST)
                            .body("There is already a team with the same name!");
                }

                Team team = new Team();

                team.setName(teamDTO.getName());
                team.setDescription(teamDTO.getDescription());
                team.setOwnerId(userId);
                team.setCreatedAt(System.currentTimeMillis());

                teamRepository.save(team);

                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body("Team created successfully!");
            }
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Team creation failed!");
    }

    @Override
    public ResponseEntity<TeamDTO> getTeam(Integer teamId, HttpServletRequest request) {
        Optional<Team> team = teamRepository.findById(teamId);
        if (team.isPresent()){
            TeamDTO teamDTO = new TeamDTO();

            teamDTO.setId(team.get().getId());
            teamDTO.setName(team.get().getName());
            teamDTO.setDescription(team.get().getDescription());
            teamDTO.setOwnerId(team.get().getOwnerId());
            teamDTO.setCreatedAt(team.get().getCreatedAt());

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(teamDTO);

        }
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(null);
    }

    @Override
    public ResponseEntity<String> updateTeam(Integer teamId, TeamDTO teamDTO, HttpServletRequest request) {

        if(isValidNameDescriptionTeamDTO(teamDTO)){
            Optional<Team> existingTeamOpt = teamRepository.findById(teamId);

            if (existingTeamOpt.isPresent()) {
                Team existingTeam = existingTeamOpt.get();

                if (existingTeam.getName().equals(teamDTO.getName()) && teamRepository.findByName(teamDTO.getName()).isPresent()) {
                    return ResponseEntity
                            .status(HttpStatus.BAD_REQUEST)
                            .body("There is already a team with the name!");
                }

                existingTeam.setName(teamDTO.getName());
                existingTeam.setDescription(teamDTO.getDescription());

                teamRepository.save(existingTeam);

                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body("Team updated successfully!");
            }else{
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("Team not found!");
            }
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Something went wrong!");
    }

    //to-do: delete team users and add users check
    @Override
    public ResponseEntity<String> deleteTeam(Integer teamId, HttpServletRequest request) {

        Optional<Team> team = teamRepository.findById(teamId);
        if (team.isPresent()){
            if(Objects.equals(team.get().getOwnerId(), Utils.getUserId(request))){
                teamRepository.delete(team.get());
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body("Team deleted");
            }else{
                return ResponseEntity
                        .status(HttpStatus.FORBIDDEN)
                        .body("Only owner can delete team!");
            }
        }
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("Team not found!");
    }
}
