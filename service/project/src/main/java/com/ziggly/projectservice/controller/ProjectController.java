package com.ziggly.projectservice.controller;

import com.ziggly.model.dto.ProjectDTO;
import com.ziggly.projectservice.api.ProjectService;
import com.ziggly.projectservice.service.ProjectServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProjectController implements ProjectService {

    @Autowired
    private ProjectServiceImpl projectService;

    @Override
    public ResponseEntity<String> createProject(ProjectDTO projectDTO, HttpServletRequest request) {
        return projectService.createProject(projectDTO, request);
    }

    @Override
    public ResponseEntity<ProjectDTO> getProject(Integer projectId, HttpServletRequest request) {
        return projectService.getProject(projectId, request);
    }

    @Override
    public ResponseEntity<String> updateProject(Integer projectId, ProjectDTO projectDTO, HttpServletRequest request) {
        return projectService.updateProject(projectId, projectDTO, request);
    }

    @Override
    public ResponseEntity<String> deleteProject(Integer projectId, HttpServletRequest request) {
        return projectService.deleteProject(projectId, request);
    }

    @Override
    public ResponseEntity<List<ProjectDTO>> getAllProjects(HttpServletRequest request) {
        return projectService.getAllProjects(request);
    }
}
