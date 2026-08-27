package com.example.smart_task_manager.Controller;

import com.example.smart_task_manager.Dto.TaskRequest;
import com.example.smart_task_manager.Dto.TaskResponse;
import com.example.smart_task_manager.Dto.TaskUpdateRequest;
import com.example.smart_task_manager.Entity.Priority;
import com.example.smart_task_manager.Entity.Status;
import com.example.smart_task_manager.Entity.User;
import com.example.smart_task_manager.Exception.UserNotFoundException;
import com.example.smart_task_manager.Repository.UserRepository;
import com.example.smart_task_manager.Service.TaskService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService service;
    private final UserRepository userRepository;

    public TaskController(TaskService service, UserRepository userRepository) {
        this.service = service;
        this.userRepository = userRepository;
    }

    private User getAuthenticatedUser(Authentication authentication) {
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found for email: " + email));
    }

    private boolean isPrivileged(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_MANAGER"));
    }

    //create task: Only MANAGER and ADMIN can create and assign tasks
    @PostMapping
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    public ResponseEntity<String> createTask(
            @Valid @RequestBody TaskRequest request) {

        log.info("Create task request received for assigned user id: {}", request.userId());

        service.createTask(request);

        log.info("Task created successfully for user id: {}", request.userId());

        return ResponseEntity.ok("Task Created Successfully");
    }

    //get tasks: ROLE_USER gets only their own tasks; ADMIN and MANAGER get all tasks
    @GetMapping
    @PreAuthorize("hasAnyRole('USER','MANAGER','ADMIN')")
    public ResponseEntity<List<TaskResponse>> getTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size,
            Authentication authentication
    ) {
        if (!isPrivileged(authentication)) {
            User currentUser = getAuthenticatedUser(authentication);
            return ResponseEntity.ok(service.getTasksByUser(currentUser.getId()));
        }

        return ResponseEntity.ok(
                service.getTasks(page, size)
        );
    }

    //get task by id: USER can only view their own task
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','MANAGER','ADMIN')")
    public ResponseEntity<TaskResponse> getTask(
            @PathVariable Long id,
            Authentication authentication) {

        TaskResponse task = service.getTask(id);

        if (!isPrivileged(authentication)) {
            User currentUser = getAuthenticatedUser(authentication);
            if (!task.userId().equals(currentUser.getId())) {
                throw new AccessDeniedException("You are not authorized to view another user's task");
            }
        }

        return ResponseEntity.ok(task);
    }

    //update task: USER can ONLY update status of their own task; ADMIN/MANAGER can update all fields
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','MANAGER','ADMIN')")
    public ResponseEntity<String> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskUpdateRequest request,
            Authentication authentication) {

        TaskResponse existingTask = service.getTask(id);

        if (!isPrivileged(authentication)) {
            User currentUser = getAuthenticatedUser(authentication);
            if (!existingTask.userId().equals(currentUser.getId())) {
                throw new AccessDeniedException("You are not authorized to modify another user's task");
            }

            // Standard USER can ONLY change status; preserve original title, description, priority, dueDate, userId
            TaskUpdateRequest statusOnlyRequest = new TaskUpdateRequest(
                    existingTask.title(),
                    existingTask.description(),
                    existingTask.priority(),
                    request.status(),
                    existingTask.dueDate(),
                    existingTask.userId()
            );

            service.updateTask(id, statusOnlyRequest);
            log.info("Task status updated by user. Task id: {}, Status: {}", id, request.status());
            return ResponseEntity.ok("Task Status Updated Successfully");
        }

        log.info("Update task request received. Task id: {}", id);

        service.updateTask(id, request);

        log.info("Task updated successfully. Task id: {}", id);

        return ResponseEntity.ok("Task Updated Successfully");
    }

    //delete task: Only MANAGER and ADMIN
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    public ResponseEntity<String> deleteTask(
            @PathVariable Long id) {

        log.info("Delete task request received. Task id: {}", id);

        service.deleteTask(id);

        log.info("Task deleted successfully. Task id: {}", id);

        return ResponseEntity.ok("Task Deleted Successfully");
    }

    //get user task by id
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('USER','MANAGER','ADMIN')")
    public ResponseEntity<List<TaskResponse>> getUserTasks(
            @PathVariable Long userId,
            Authentication authentication) {

        if (!isPrivileged(authentication)) {
            User currentUser = getAuthenticatedUser(authentication);
            if (!userId.equals(currentUser.getId())) {
                throw new AccessDeniedException("You are not authorized to view another user's tasks");
            }
        }

        return ResponseEntity.ok(
                service.getTasksByUser(userId));
    }

    //get user task by status
    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('USER','MANAGER','ADMIN')")
    public ResponseEntity<List<TaskResponse>> getTasksByStatus(
            @PathVariable Status status,
            Authentication authentication) {

        List<TaskResponse> list = service.getTasksByStatus(status);

        if (!isPrivileged(authentication)) {
            User currentUser = getAuthenticatedUser(authentication);
            list = list.stream()
                    .filter(t -> t.userId().equals(currentUser.getId()))
                    .toList();
        }

        return ResponseEntity.ok(list);
    }

    //get task by priority
    @GetMapping("/priority/{priority}")
    @PreAuthorize("hasAnyRole('USER','MANAGER','ADMIN')")
    public ResponseEntity<List<TaskResponse>> getTasksByPriority(
            @PathVariable Priority priority,
            Authentication authentication) {

        List<TaskResponse> list = service.getTasksByPriority(priority);

        if (!isPrivileged(authentication)) {
            User currentUser = getAuthenticatedUser(authentication);
            list = list.stream()
                    .filter(t -> t.userId().equals(currentUser.getId()))
                    .toList();
        }

        return ResponseEntity.ok(list);
    }

    //search by title
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('USER','MANAGER','ADMIN')")
    public ResponseEntity<List<TaskResponse>> searchTask(
            @RequestParam String keyword,
            Authentication authentication
    ) {
        List<TaskResponse> list = service.searchTasks(keyword);

        if (!isPrivileged(authentication)) {
            User currentUser = getAuthenticatedUser(authentication);
            list = list.stream()
                    .filter(t -> t.userId().equals(currentUser.getId()))
                    .toList();
        }

        return ResponseEntity.ok(list);
    }

    //sorting task
    @GetMapping("/sort")
    @PreAuthorize("hasAnyRole('USER','MANAGER','ADMIN')")
    public ResponseEntity<List<TaskResponse>> sortTasks(Authentication authentication) {

        List<TaskResponse> list = service.sortTasks();

        if (!isPrivileged(authentication)) {
            User currentUser = getAuthenticatedUser(authentication);
            list = list.stream()
                    .filter(t -> t.userId().equals(currentUser.getId()))
                    .toList();
        }

        return ResponseEntity.ok(list);
    }
}