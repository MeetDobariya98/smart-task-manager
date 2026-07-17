package com.example.smart_task_manager.Service;

import com.example.smart_task_manager.Dto.TaskRequest;
import com.example.smart_task_manager.Dto.TaskResponse;
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
}