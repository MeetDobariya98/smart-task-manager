package com.example.smart_task_manager.Service;

import com.example.smart_task_manager.Dto.UserRequest;
import com.example.smart_task_manager.Dto.UserResponse;

import java.util.List;

public interface UserService {

    void createUser(UserRequest request);

    List<UserResponse> getUsers();

    UserResponse getUser(Long id);

    void deleteUser(Long id);
}