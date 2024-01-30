package com.ziggly.teamservice.repository;

import com.ziggly.model.team.TeamUser;
import org.springframework.data.jpa.repository.JpaRepository;
import reactor.util.annotation.NonNull;

import java.util.List;
import java.util.Optional;

public interface TeamUserRepository extends JpaRepository<TeamUser, Integer> {
    @NonNull
    Optional<TeamUser> findById(Integer id);
    Optional<TeamUser> findByUserIdAndTeamId(Integer userId, Integer teamId);

    List<TeamUser> findByTeamId(Integer teamId);

}
