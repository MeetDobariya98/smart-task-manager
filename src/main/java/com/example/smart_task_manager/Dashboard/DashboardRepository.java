package com.example.smart_task_manager.Dashboard;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DashboardRepository {

    private final JdbcTemplate jdbcTemplate;

    public DashboardRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //total user
    public Long getTotalUsers() {

        String sql = "SELECT COUNT(*) FROM users";

        return jdbcTemplate.queryForObject(
                sql,
                Long.class
        );
    }

    //total task
    public Long getTotalTasks() {

        String sql = "SELECT COUNT(*) FROM tasks";

        return jdbcTemplate.queryForObject(
                sql,
                Long.class
        );
    }

    //completed task
    public Long getCompletedTasks() {

        String sql = """
            SELECT COUNT(*)
            FROM tasks
            WHERE status='COMPLETED'
            """;

        return jdbcTemplate.queryForObject(
                sql,
                Long.class
        );
    }

    //today's task
    public Long getTodayCreatedTasks() {

        String sql = """
            SELECT COUNT(*)
            FROM tasks
            WHERE DATE(created_at)=CURRENT_DATE
            """;

        return jdbcTemplate.queryForObject(
                sql,
                Long.class
        );
    }

    public Long getPendingTasks() {

        String sql = """
            SELECT COUNT(*)
            FROM tasks
            WHERE status='PENDING'
            """;

        return jdbcTemplate.queryForObject(
                sql,
                Long.class
        );
    }

    public Long getInProgressTasks() {

        String sql = """
            SELECT COUNT(*)
            FROM tasks
            WHERE status='IN_PROGRESS'
            """;

        return jdbcTemplate.queryForObject(
                sql,
                Long.class
        );
    }


}
