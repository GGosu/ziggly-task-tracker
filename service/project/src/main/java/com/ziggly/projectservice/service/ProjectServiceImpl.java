package com.ziggly.projectservice.service;

import com.ziggly.model.dto.ProjectDTO;
import com.ziggly.model.enums.ProjectStatus;
import com.ziggly.model.project.Project;
import com.ziggly.model.utils.Utils;
import com.ziggly.projectservice.api.ProjectService;
import com.ziggly.projectservice.repository.ProjectRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepository projectRepository;
    private boolean isValidDateRange(Long startDate, Long endDate){
        if (startDate == null || endDate == null) {
            return false;
        }
        if (startDate > endDate) {
            return false;
        }
        return true;
    }

    @Override
    public ResponseEntity<String> createProject(ProjectDTO projectDTO, HttpServletRequest request) {

        if (projectDTO == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Project data is missing!");
        }
        if (projectDTO.getName() == null || projectDTO.getName().trim().isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Project name is required!");
        }
        if (projectDTO.getTeamId() == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Team ID is required!");
        }
        if(!isValidDateRange(projectDTO.getStartDate(), projectDTO.getEndDate())){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Invalid Date Range!");
        }

        Project project = new Project();

        project.setName(projectDTO.getName());
        project.setDescription(projectDTO.getDescription());
        project.setStartDate(projectDTO.getStartDate());
        project.setEndDate(projectDTO.getEndDate());
        project.setStatus(ProjectStatus.ACTIVE);
        project.setCreatedAt(System.currentTimeMillis());
        project.setUpdatedAt(System.currentTimeMillis());
        project.setTeamId(projectDTO.getTeamId());
        project.setOwnerId(Utils.getUserId(request));

        projectRepository.save(project);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Project created successfully!");

    }

    @Override
    public ResponseEntity<ProjectDTO> getProject(Integer projectId, HttpServletRequest request) {
        Project project = projectRepository.findById(projectId).orElse(null);
        if (project == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
        ProjectDTO projectDTO = new ProjectDTO();
        BeanUtils.copyProperties(project, projectDTO);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(projectDTO);
    }

    @Override
    public ResponseEntity<String> updateProject(Integer projectId, ProjectDTO projectDTO, HttpServletRequest request) {
        if (projectDTO == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Project data is missing!");
        }

        Optional<Project> projectOptional = projectRepository.findById(projectId);
        if (projectOptional.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Project not found!");
        }

        Project project = projectOptional.get();
        // to do: add check for status, name and description

        project.setName(projectDTO.getName());
        project.setDescription(projectDTO.getDescription());

        if(isValidDateRange(projectDTO.getStartDate(), projectDTO.getEndDate())){
            project.setStartDate(projectDTO.getStartDate());
            project.setEndDate(projectDTO.getEndDate());
        }

        project.setStatus(projectDTO.getStatus());

        project.setUpdatedAt(System.currentTimeMillis());

        projectRepository.save(project);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Project updated successfully!");
    }

    @Override
    public ResponseEntity<String> deleteProject(Integer projectId, HttpServletRequest request) {
        if (!projectRepository.existsById(projectId)) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Project not found!");
        }

        projectRepository.deleteById(projectId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Project deleted successfully!");
    }

    @Override
    public ResponseEntity<List<ProjectDTO>> getAllProjects(HttpServletRequest request) {
        List<Project> projects = projectRepository.findAll();
        List<ProjectDTO> projectDTOs = projects.stream()
                .map(project -> {
                    ProjectDTO dto = new ProjectDTO();
                    BeanUtils.copyProperties(project, dto);
                    return dto;
                }).collect(Collectors.toList());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(projectDTOs);
    }
}
