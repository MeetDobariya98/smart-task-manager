package com.example.smart_task_manager.Dto;

import com.example.smart_task_manager.Entity.Priority;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record TaskRequest(

        @NotBlank(message = "Title is required")
        String title,

        String description,

        @NotNull(message = "Priority is required")
        Priority priority,

        @FutureOrPresent(message = "Due date cannot be in the past")
        LocalDate dueDate,

        @NotNull(message = "User ID is required")
        Long userId
) {
}