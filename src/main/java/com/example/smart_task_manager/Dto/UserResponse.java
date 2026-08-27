package com.example.smart_task_manager.Dto;

import com.example.smart_task_manager.Entity.Role;

public record UserResponse(

        Long id,

        String name,

        String email,

        Role role
) {
}