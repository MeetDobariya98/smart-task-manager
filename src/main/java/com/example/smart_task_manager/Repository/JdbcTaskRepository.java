package com.example.smart_task_manager.Repository;

import com.example.smart_task_manager.Dto.TaskRequest;
import com.example.smart_task_manager.Entity.Status;
import com.example.smart_task_manager.Entity.Task;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JdbcTaskRepository implements TaskRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTaskRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Task> taskRowMapper = (rs, rowNum) -> {

        Task task = new Task();

        task.setId(rs.getLong("id"));
        task.setTitle(rs.getString("title"));
        task.setDescription(rs.getString("description"));
        task.setPriority(
                Enum.valueOf(
                        com.example.smart_task_manager.Entity.Priority.class,
                        rs.getString("priority")
                ));

        task.setStatus(
                Enum.valueOf(
                        Status.class,
                        rs.getString("status")
                ));

        task.setDueDate(rs.getDate("due_date").toLocalDate());

        task.setUserId(rs.getLong("user_id"));

        task.setCreatedAt(
                rs.getTimestamp("created_at")
                        .toLocalDateTime());

        return task;
    };

    @Override
    public void save(TaskRequest request) {

        String sql = """
                INSERT INTO tasks
                (title,
                 description,
                 priority,
                 status,
                 due_date,
                 user_id)
                VALUES (?,?,?,?,?,?)
                """;

        jdbcTemplate.update(
                sql,
                request.title(),
                request.description(),
                request.priority().name(),
                Status.PENDING.name(),
                request.dueDate(),
                request.userId()
        );
    }

    @Override
    public List<Task> findAll() {

        String sql = """
                SELECT *
                FROM tasks
                ORDER BY id
                """;

        return jdbcTemplate.query(sql, taskRowMapper);
    }
}