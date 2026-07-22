package com.example.smart_task_manager.Controller;

import com.example.smart_task_manager.Dto.TaskRequest;
import com.example.smart_task_manager.Dto.TaskResponse;
import com.example.smart_task_manager.Dto.TaskUpdateRequest;
import com.example.smart_task_manager.Entity.Priority;
import com.example.smart_task_manager.Entity.Status;
import com.example.smart_task_manager.Service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    //create task
    @PostMapping
    @PreAuthorize("hasAnyRole('USER','MANAGER','ADMIN')")
    public ResponseEntity<String> createTask(
            @Valid @RequestBody TaskRequest request) {

        service.createTask(request);

        return ResponseEntity.ok("Task Created Successfully");
    }

    //get task
    @GetMapping
    @PreAuthorize("hasAnyRole('USER','MANAGER','ADMIN')")
    public ResponseEntity<List<TaskResponse>> getTasks(

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "5")
            int size
    ) {

        return ResponseEntity.ok(
                service.getTasks(page, size)
        );
    }

    //get task by id
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','MANAGER','ADMIN')")
    public ResponseEntity<TaskResponse> getTask(
            @PathVariable Long id) {

        return ResponseEntity.ok(service.getTask(id));
    }

    //update task
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','MANAGER','ADMIN')")
    public ResponseEntity<String> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskUpdateRequest request) {

        service.updateTask(id, request);

        return ResponseEntity.ok("Task Updated Successfully");
    }

    //delete task
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    public ResponseEntity<String> deleteTask(
            @PathVariable Long id) {

        service.deleteTask(id);

        return ResponseEntity.ok("Task Deleted Successfully");
    }

    //get user task by id
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('USER','MANAGER','ADMIN')")
    public ResponseEntity<List<TaskResponse>> getUserTasks(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                service.getTasksByUser(userId));
    }

    //get user task by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<TaskResponse>> getTasksByStatus(
            @PathVariable Status status) {

        return ResponseEntity.ok(
                service.getTasksByStatus(status));
    }

    //get task by priority
    @GetMapping("/priority/{priority}")
    @PreAuthorize("hasAnyRole('USER','MANAGER','ADMIN')")
    public ResponseEntity<List<TaskResponse>> getTasksByPriority(
            @PathVariable Priority priority) {

        return ResponseEntity.ok(
                service.getTasksByPriority(priority));
    }

    //search by tittle
    @PreAuthorize("hasAnyRole('USER','MANAGER','ADMIN')")
    @GetMapping("/search")
    public ResponseEntity<List<TaskResponse>> searchTask(

            @RequestParam String keyword
    ) {

        return ResponseEntity.ok(
                service.searchTasks(keyword)
        );
    }

    //sorting task
    @GetMapping("/sort")
    @PreAuthorize("hasAnyRole('USER','MANAGER','ADMIN')")
    public ResponseEntity<List<TaskResponse>> sortTasks() {

        return ResponseEntity.ok(
                service.sortTasks()
        );
    }
}