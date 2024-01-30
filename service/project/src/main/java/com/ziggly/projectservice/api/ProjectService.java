package com.ziggly.projectservice.api;

import com.ziggly.model.dto.ProjectDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface ProjectService {
    @PostMapping("/api/projects")
    ResponseEntity<String> createProject(@RequestBody ProjectDTO projectDTO, HttpServletRequest request);

    @GetMapping("/api/projects/{projectId}")
    ResponseEntity<ProjectDTO> getProject(@PathVariable Integer projectId, HttpServletRequest request);

    @PutMapping("/api/projects/{projectId}")
    ResponseEntity<String> updateProject(@PathVariable Integer projectId, @RequestBody ProjectDTO projectDTO, HttpServletRequest request);

    @DeleteMapping("/api/projects/{projectId}")
    ResponseEntity<String> deleteProject(@PathVariable Integer projectId, HttpServletRequest request);

    @GetMapping("/api/projects")
    ResponseEntity<List<ProjectDTO>> getAllProjects(HttpServletRequest request);
}
