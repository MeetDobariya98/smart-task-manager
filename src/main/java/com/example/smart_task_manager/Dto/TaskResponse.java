package com.example.smart_task_manager.Dto;

import com.example.smart_task_manager.Entity.Priority;
import com.example.smart_task_manager.Entity.Status;


import java.time.LocalDate;

public record TaskResponse(

        Long id,

        String title,

        String description,

        Priority priority,

        Status status,

        LocalDate dueDate
) {
}