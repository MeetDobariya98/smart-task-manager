package com.example.smart_task_manager.Entity;

import java.time.LocalDateTime;

public class AuditLog {

    private Long id;

    private String action;

    private LocalDateTime createdAt;

    public AuditLog() {}

    public AuditLog(Long id,
                    String action,
                    LocalDateTime createdAt) {
        this.id = id;
        this.action = action;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}