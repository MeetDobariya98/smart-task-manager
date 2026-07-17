package com.example.smart_task_manager.Controller;

import com.example.smart_task_manager.Dto.UserRequest;
import com.example.smart_task_manager.Dto.UserResponse;
import com.example.smart_task_manager.Dto.UserUpdateRequest;
import com.example.smart_task_manager.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    //Create User
    @PostMapping
    public String createUser(@Valid @RequestBody UserRequest request) {

        service.createUser(request);

        return "User Created Successfully";
    }

    //Get User
    @GetMapping
    public List<UserResponse> getUsers() {
        return service.getUsers();
    }

    //get user by id
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(
            @PathVariable Long id) {

        return ResponseEntity.ok(service.getUser(id));
    }

    //delete user
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(
            @PathVariable Long id) {

        service.deleteUser(id);

        return ResponseEntity.ok("User Deleted Successfully");
    }

    //update user
    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(
            @PathVariable Long id,
            @Valid
            @RequestBody UserUpdateRequest request) {

        service.updateUser(id,
                request);

        return ResponseEntity.ok(
                "User Updated Successfully");
    }

}