package com.ziggly.taskservice.service;

import com.ziggly.model.dto.TaskDTO;
import com.ziggly.model.task.Task;
import com.ziggly.model.utils.Utils;
import com.ziggly.taskservice.api.TaskService;
import com.ziggly.taskservice.repository.TaskRepository;
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
public class TaskServiceImpl implements TaskService {
    @Autowired
    private TaskRepository taskRepository;
    @Override
    public ResponseEntity<String> createTask(TaskDTO taskDTO, HttpServletRequest request) {

        if (taskDTO == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Task creation failed!");
        }

        if (taskDTO.getName() == null || taskDTO.getName().trim().isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Task creation failed: Task name is required!");
        }

        if (taskDTO.getProjectId() == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Task creation failed: Project ID is required!");
        }

        if (taskDTO.getStatus() == null || taskDTO.getStatus().trim().isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Task creation failed: Status is required!");

        }

        Task task = new Task();

        task.setName(taskDTO.getName());
        task.setDescription(taskDTO.getDescription());
        task.setStatus(taskDTO.getStatus());
        task.setPriority(taskDTO.getPriority());

        task.setProjectId(taskDTO.getProjectId());
        task.setAssigneeId(Utils.getUserId(request));

        task.setDeadline(taskDTO.getDeadline());
        task.setAssigneeId(taskDTO.getAssigneeId());

        task.setCreatedAt(System.currentTimeMillis());
        task.setUpdatedAt(System.currentTimeMillis());

        taskRepository.save(task);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Task created successfully!");
    }

    @Override
    public ResponseEntity<TaskDTO> getTask(Integer taskId, HttpServletRequest request) {
        Optional<Task> taskOptional = taskRepository.findById(taskId);

        if (taskOptional.isEmpty()) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(null);
        }

        Task task = taskOptional.get();
        TaskDTO taskDTO = new TaskDTO();

        BeanUtils.copyProperties(task, taskDTO);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(taskDTO);
    }

    @Override
    public ResponseEntity<String> updateTask(Integer taskId, TaskDTO taskDTO, HttpServletRequest request) {

        Optional<Task> taskOptional = taskRepository.findById(taskId);
        if (taskOptional.isEmpty()) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Task not found!");

        }

        Task task = taskOptional.get();
        task.setName(taskDTO.getName());
        task.setDescription(taskDTO.getDescription());
        task.setStatus(taskDTO.getStatus());
        task.setPriority(taskDTO.getPriority());
        task.setDeadline(taskDTO.getDeadline());

        task.setUpdatedAt(System.currentTimeMillis());

        taskRepository.save(task);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Task updated successfully!");

    }

    @Override
    public ResponseEntity<String> deleteTask(Integer taskId, HttpServletRequest request) {
        if (!taskRepository.existsById(taskId)) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Task not found!");
        }

        taskRepository.deleteById(taskId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Task deleted successfully!");
    }

    @Override
    public ResponseEntity<List<TaskDTO>> getAllTasks(HttpServletRequest request) {
        List<Task> tasks = taskRepository.findAll();
        List<TaskDTO> taskDTOs = tasks.stream()
                .map(task -> {
                    TaskDTO dto = new TaskDTO();
                    BeanUtils.copyProperties(task, dto);
                    return dto;
                }).collect(Collectors.toList());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(taskDTOs);
    }

}
