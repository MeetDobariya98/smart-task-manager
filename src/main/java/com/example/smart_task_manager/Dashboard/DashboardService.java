package com.example.smart_task_manager.Dashboard;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DashboardService {

    private final DashboardRepository repository;

    public DashboardService(DashboardRepository repository) {
        this.repository = repository;
    }

    public DashboardResponse getDashboard() {

        log.info("Preparing dashboard statistics");

        Long totalUsers = repository.getTotalUsers();
        Long totalTasks = repository.getTotalTasks();
        Long completedTasks = repository.getCompletedTasks();
        Long pendingTasks = repository.getPendingTasks();
        Long inProgressTasks = repository.getInProgressTasks();
        Long todayCreatedTasks = repository.getTodayCreatedTasks();

        double completionPercentage =
                totalTasks == 0 ? 0 :
                        (completedTasks * 100.0) / totalTasks;

        double pendingPercentage =
                totalTasks == 0 ? 0 :
                        (pendingTasks * 100.0) / totalTasks;

        double inProgressPercentage =
                totalTasks == 0 ? 0 :
                        (inProgressTasks * 100.0) / totalTasks;

        DashboardResponse response = new DashboardResponse(

                totalUsers,
                totalTasks,
                completedTasks,
                pendingTasks,
                inProgressTasks,
                todayCreatedTasks,
                completionPercentage,
                pendingPercentage,
                inProgressPercentage
        );

        log.info("Dashboard statistics prepared successfully");

        return response;
    }
}