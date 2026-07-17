package com.example.smart_task_manager.Service;

import com.example.smart_task_manager.Dto.TaskRequest;
import com.example.smart_task_manager.Dto.TaskResponse;

import java.util.List;

public interface TaskService {

    void createTask(TaskRequest request);

    List<TaskResponse> getTasks();
}