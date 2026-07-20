package com.example.smart_task_manager.Repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcAuditRepository
        implements AuditRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcAuditRepository(
            JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(String action) {

        String sql = """
                INSERT INTO audit_logs(action)
                VALUES(?)
                """;

        jdbcTemplate.update(sql, action);
    }
}