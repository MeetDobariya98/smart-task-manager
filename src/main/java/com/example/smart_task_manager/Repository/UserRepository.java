package com.example.smart_task_manager.Repository;

import com.example.smart_task_manager.Dto.UserRequest;
import com.example.smart_task_manager.Entity.User;

import java.util.List;

public interface UserRepository {

    void save(UserRequest request);

    List<User> findAll();
}