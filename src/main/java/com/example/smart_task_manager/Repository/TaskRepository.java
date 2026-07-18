package com.example.smart_task_manager.Repository;

import com.example.smart_task_manager.Dto.TaskRequest;
import com.example.smart_task_manager.Dto.TaskUpdateRequest;
import com.example.smart_task_manager.Entity.Priority;
import com.example.smart_task_manager.Entity.Status;
import com.example.smart_task_manager.Entity.Task;


import java.util.List;
import java.util.Optional;

public interface TaskRepository {

    //create task
    void save(TaskRequest request);

    //get task
    List<Task> findAll();

    //get task by id
    Optional<Task> findById(Long id);

    //update task
    void update(Long id, TaskUpdateRequest request);

    //delete task
    void delete(Long id);

    //find user by id
    List<Task> findByUserId(Long userId);

    //find user by status
    List<Task> findByStatus(Status status);

    //find user by priority
    List<Task> findByPriority(Priority priority);
}