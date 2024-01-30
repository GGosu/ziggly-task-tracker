package com.ziggly.projectservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ziggly.model.project.Project;

public interface ProjectRepository extends JpaRepository<Project, Integer>{

}
