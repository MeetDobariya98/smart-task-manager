package com.example.smart_task_manager.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(

        @NotBlank(message = "Name is required")
        String name,

        @Email(message = "Invalid Email")
        @NotBlank(message = "Email is required")
        String email,

        @Size(min = 6)
        String password
) {
}