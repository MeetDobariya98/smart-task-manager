package com.example.smart_task_manager.Repository;

import com.example.smart_task_manager.Dto.TaskRequest;
import com.example.smart_task_manager.Entity.Task;


import java.util.List;

public interface TaskRepository {

    void save(TaskRequest request);

    List<Task> findAll();
}