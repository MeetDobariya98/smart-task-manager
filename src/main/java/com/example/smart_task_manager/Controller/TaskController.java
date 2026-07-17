package com.example.smart_task_manager.Controller;

import com.example.smart_task_manager.Dto.TaskRequest;
import com.example.smart_task_manager.Dto.TaskResponse;
import com.example.smart_task_manager.Service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<String> createTask(
            @Valid @RequestBody TaskRequest request) {

        service.createTask(request);

        return ResponseEntity.ok("Task Created Successfully");
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getTasks() {

        return ResponseEntity.ok(service.getTasks());
    }
}