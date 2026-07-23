package com.example.smart_task_manager.Dashboard;

public record DashboardResponse(

        Long totalUsers,

        Long totalTasks,

        Long completedTasks,

        Long pendingTasks,

        Long inProgressTasks,

        Long todayCreatedTasks,

        Double completionPercentage,

        Double pendingPercentage,

        Double inProgressPercentage

){}