package com.example.smart_task_manager.Controller;

import com.example.smart_task_manager.Dto.UserRequest;
import com.example.smart_task_manager.Dto.UserResponse;
import com.example.smart_task_manager.Dto.UserUpdateRequest;
import com.example.smart_task_manager.Service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    //Get User
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getUsers() {
        return service.getUsers();
    }

    //get user by id
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','MANAGER','ADMIN')")
    public ResponseEntity<UserResponse> getUser(
            @PathVariable Long id) {

        return ResponseEntity.ok(service.getUser(id));
    }

    //delete user
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteUser(
            @PathVariable Long id) {

        log.info("Delete user request received for user id: {}", id);

        service.deleteUser(id);

        log.info("User deleted successfully. User id: {}", id);

        return ResponseEntity.ok("User Deleted Successfully");
    }

    //update user
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<String> updateUser(
            @PathVariable Long id,
            @Valid
            @RequestBody UserUpdateRequest request) {

        log.info("Update request received for user id: {}", id);

        service.updateUser(id,
                request);

        log.info("User updated successfully. User id: {}", id);

        return ResponseEntity.ok(
                "User Updated Successfully");
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String admin() {

        return "Admin Page";
    }

}