package com.example.smart_task_manager.Service;

import com.example.smart_task_manager.Dto.UserRequest;
import com.example.smart_task_manager.Dto.UserResponse;
import com.example.smart_task_manager.Dto.UserUpdateRequest;

import java.util.List;

public interface UserService {

    //create user
    void createUser(UserRequest request);

    //get all user
    List<UserResponse> getUsers();

    //get user by id
    UserResponse getUser(Long id);

    //delete user
    void deleteUser(Long id);

    //update user
    void updateUser(Long id,
                    UserUpdateRequest request);
}