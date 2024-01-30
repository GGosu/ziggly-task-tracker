package com.ziggly.teamservice.repository;

import com.ziggly.model.team.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import reactor.util.annotation.NonNull;

import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Integer> {
    @NonNull
    Optional<Team> findById(Integer id);
    Optional<Team> findByName(String name);
}
