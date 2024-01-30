package com.ziggly.taskservice.controller;

import com.ziggly.model.dto.TaskDTO;
import com.ziggly.taskservice.api.TaskService;
import com.ziggly.taskservice.service.TaskServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TaskController implements TaskService {
    @Autowired
    private TaskServiceImpl taskService;

    @Override
    public ResponseEntity<String> createTask(TaskDTO taskDTO, HttpServletRequest request) {
        return taskService.createTask(taskDTO, request);
    }

    @Override
    public ResponseEntity<TaskDTO> getTask(Integer taskId, HttpServletRequest request) {
        return taskService.getTask(taskId, request);
    }

    @Override
    public ResponseEntity<String> updateTask(Integer taskId, TaskDTO taskDTO, HttpServletRequest request) {
        return taskService.updateTask(taskId, taskDTO, request);
    }

    @Override
    public ResponseEntity<String> deleteTask(Integer taskId, HttpServletRequest request) {
        return taskService.deleteTask(taskId, request);
    }

    @Override
    public ResponseEntity<List<TaskDTO>> getAllTasks(HttpServletRequest request) {
        return taskService.getAllTasks(request);
    }
}
