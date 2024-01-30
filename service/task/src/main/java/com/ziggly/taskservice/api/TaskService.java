package com.ziggly.taskservice.api;

import com.ziggly.model.dto.TaskDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

public interface TaskService {

    @PostMapping("/api/tasks")
    ResponseEntity<String> createTask(@RequestBody TaskDTO taskDTO, HttpServletRequest request);

    @GetMapping("/api/tasks/{taskId}")
    ResponseEntity<TaskDTO> getTask(@PathVariable Integer taskId, HttpServletRequest request);

    @PutMapping("/api/tasks/{taskId}")
    ResponseEntity<String> updateTask(@PathVariable Integer taskId, @RequestBody TaskDTO taskDTO, HttpServletRequest request);

    @DeleteMapping("/api/tasks/{taskId}")
    ResponseEntity<String> deleteTask(@PathVariable Integer taskId, HttpServletRequest request);

    @GetMapping("/api/tasks")
    ResponseEntity<List<TaskDTO>> getAllTasks(HttpServletRequest request);

}
