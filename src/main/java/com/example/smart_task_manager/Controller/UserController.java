package com.example.smart_task_manager.Controller;

import com.example.smart_task_manager.Dto.UserRequest;
import com.example.smart_task_manager.Dto.UserResponse;
import com.example.smart_task_manager.Service.UserService;
import jakarta.validation.Valid;
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

    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable String id) {
        return service.getUserById(id);
    }
}