package com.example.smart_task_manager.Service;

import com.example.smart_task_manager.Dto.TaskRequest;
import com.example.smart_task_manager.Dto.TaskResponse;
import com.example.smart_task_manager.Dto.TaskUpdateRequest;
import com.example.smart_task_manager.Entity.Priority;
import com.example.smart_task_manager.Entity.Status;
import com.example.smart_task_manager.Repository.AuditRepository;
import com.example.smart_task_manager.Repository.TaskRepository;
import com.example.smart_task_manager.Repository.UserRepository;

import java.util.List;

public interface TaskService {

    //create task
    void createTask(TaskRequest request);

    //get task
    List<TaskResponse> getTasks();

    //get task by id
    TaskResponse getTask(Long id);

    //update task
    void updateTask(Long id,
                    TaskUpdateRequest request);

    //delete task
    void deleteTask(Long id);

    //find by user id
    List<TaskResponse> getTasksByUser(Long userId);

    //find by status
    List<TaskResponse> getTasksByStatus(Status status);

    //find by priority
    List<TaskResponse> getTasksByPriority(Priority priority);

    //get task page wise
    List<TaskResponse> getTasks(
            int page,
            int size
    );

    //get task by searching title
    List<TaskResponse> searchTasks(String keyword);

    //sorting task
    List<TaskResponse> sortTasks();
}