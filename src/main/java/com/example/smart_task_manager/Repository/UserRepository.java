package com.example.smart_task_manager.Repository;

import com.example.smart_task_manager.Dto.UserRequest;
import com.example.smart_task_manager.Entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    //save user
    void save(UserRequest request);

    //get user
    List<User> findAll();

    //get user by id
    Optional<User> findById(Long id);

    //delete user by id
    void delete(Long id);

}