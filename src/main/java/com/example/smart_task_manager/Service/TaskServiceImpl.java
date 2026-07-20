package com.example.smart_task_manager.Service;

import com.example.smart_task_manager.Dto.TaskRequest;
import com.example.smart_task_manager.Dto.TaskResponse;
import com.example.smart_task_manager.Dto.TaskUpdateRequest;
import com.example.smart_task_manager.Entity.Priority;
import com.example.smart_task_manager.Entity.Status;
import com.example.smart_task_manager.Entity.Task;
import com.example.smart_task_manager.Exception.TaskNotFoundException;
import com.example.smart_task_manager.Exception.UserNotFoundException;
import com.example.smart_task_manager.Repository.AuditRepository;
import com.example.smart_task_manager.Repository.TaskRepository;
import com.example.smart_task_manager.Repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository repository;
    private final UserRepository userRepository;
    private final AuditRepository auditRepository;

    private static final Logger logger =
            LoggerFactory.getLogger(TaskServiceImpl.class);

    public TaskServiceImpl(
            TaskRepository repository,
            UserRepository userRepository,
            AuditRepository auditRepository) {

        this.repository = repository;
        this.userRepository = userRepository;
        this.auditRepository = auditRepository;
    }


    @Transactional
    @Override
    public void createTask(TaskRequest request) {

        logger.debug("Checking user with id: {}", request.userId());

        if (!userRepository.existsById(request.userId())) {

            logger.error("User not found with id: {}", request.userId());

            throw new UserNotFoundException(
                    "User not found with id " + request.userId());
        }

        logger.info("Creating task with title: {}", request.title());

        repository.save(request);

        auditRepository.save("Task Created : " + request.title());

        logger.info("Task created successfully");
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

        logger.info("Updating task with id: {}", id);

        repository.findById(id)
                .orElseThrow(() ->
                        new TaskNotFoundException(
                                "Task not found with id " + id));

        repository.update(id, request);

        logger.info("Task updated successfully");
    }

    @Override
    public void deleteTask(Long id) {

        logger.info("Deleting task with id: {}", id);

        repository.findById(id)
                .orElseThrow(() ->
                        new TaskNotFoundException(
                                "Task not found with id " + id));

        repository.delete(id);

        logger.info("Task deleted successfully");
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

    @Override
    public List<TaskResponse> getTasks(
            int page,
            int size) {

        return repository.findAll(page, size)
                .stream()
                .map(this::convert)
                .toList();
    }

    @Override
    public List<TaskResponse> searchTasks(
            String keyword) {

        return repository.searchByTitle(keyword)
                .stream()
                .map(this::convert)
                .toList();
    }

    @Override
    public List<TaskResponse> sortTasks() {

        return repository.sortByDueDate()
                .stream()
                .map(this::convert)
                .toList();
    }


}