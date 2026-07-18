package com.example.smart_task_manager.Service;

import com.example.smart_task_manager.Dto.TaskRequest;
import com.example.smart_task_manager.Dto.TaskResponse;
import com.example.smart_task_manager.Dto.TaskUpdateRequest;
import com.example.smart_task_manager.Entity.Priority;
import com.example.smart_task_manager.Entity.Status;
import com.example.smart_task_manager.Entity.Task;
import com.example.smart_task_manager.Exception.TaskNotFoundException;
import com.example.smart_task_manager.Exception.UserNotFoundException;
import com.example.smart_task_manager.Repository.TaskRepository;
import com.example.smart_task_manager.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository repository;
    private final UserRepository userRepository;

    public TaskServiceImpl(TaskRepository repository,
                           UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    @Override
    public void createTask(TaskRequest request) {

        if (!userRepository.existsById(request.userId())) {
            throw new UserNotFoundException(
                    "User not found with id " + request.userId());
        }

        repository.save(request);
    }

    @Override
    public List<TaskResponse> getTasks() {

        return repository.findAll()
                .stream()
                .map(task -> new TaskResponse(
                        task.getId(),
                        task.getTitle(),
                        task.getDescription(),
                        task.getPriority(),
                        task.getStatus(),
                        task.getDueDate()
                ))
                .toList();
    }

    @Override
    public TaskResponse getTask(Long id) {

        Task task = repository.findById(id)
                .orElseThrow(() ->
                        new TaskNotFoundException(
                                "Task not found with id " + id));

        return convert(task);
    }

    @Override
    public void updateTask(Long id,
                           TaskUpdateRequest request) {

        repository.findById(id)
                .orElseThrow(() ->
                        new TaskNotFoundException(
                                "Task not found with id " + id));

        repository.update(id, request);
    }

    @Override
    public void deleteTask(Long id) {

        repository.findById(id)
                .orElseThrow(() ->
                        new TaskNotFoundException(
                                "Task not found with id " + id));

        repository.delete(id);
    }

    private TaskResponse convert(Task task) {

        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getPriority(),
                task.getStatus(),
                task.getDueDate()
        );
    }

    @Override
    public List<TaskResponse> getTasksByUser(Long userId) {

        return repository.findByUserId(userId)
                .stream()
                .map(this::convert)
                .toList();
    }

    @Override
    public List<TaskResponse> getTasksByStatus(Status status) {

        return repository.findByStatus(status)
                .stream()
                .map(this::convert)
                .toList();
    }

    @Override
    public List<TaskResponse> getTasksByPriority(Priority priority) {

        return repository.findByPriority(priority)
                .stream()
                .map(this::convert)
                .toList();
    }



}