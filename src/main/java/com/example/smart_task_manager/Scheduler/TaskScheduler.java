package com.example.smart_task_manager.Scheduler;

import com.example.smart_task_manager.Entity.Task;
import com.example.smart_task_manager.Repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TaskScheduler {

    private final TaskRepository repository;

    public TaskScheduler(
            TaskRepository repository) {

        this.repository = repository;
    }

    private static final Logger logger =
            LoggerFactory.getLogger(TaskScheduler.class);

    @Scheduled(cron = "0 0 10 * * *", zone = "Asia/Kolkata")
    public void checkTasks() {

        logger.info("Checking overdue tasks...");

        List<Task> tasks =
                repository.findOverdueTasks();

        logger.info(
                "{} overdue task(s) found",
                tasks.size()
        );

        tasks.forEach(task ->
                logger.info(
                        "Task: {}",
                        task.getTitle()
                ));
    }
}