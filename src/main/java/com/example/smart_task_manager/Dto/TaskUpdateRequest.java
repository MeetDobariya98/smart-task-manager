package com.example.smart_task_manager.Dto;

import com.example.smart_task_manager.Entity.Priority;
import com.example.smart_task_manager.Entity.Status;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record TaskUpdateRequest(

        @NotBlank(message = "Title is required")
        String title,

        String description,

        @NotNull(message = "Priority is required")
        Priority priority,

        @NotNull(message = "Status is required")
        Status status,

        @FutureOrPresent(message = "Due date cannot be in the past")
        LocalDate dueDate
) {
}