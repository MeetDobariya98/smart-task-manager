package com.example.smart_task_manager.Repository;

import com.example.smart_task_manager.Dto.TaskRequest;
import com.example.smart_task_manager.Dto.TaskUpdateRequest;
import com.example.smart_task_manager.Entity.Priority;
import com.example.smart_task_manager.Entity.Status;
import com.example.smart_task_manager.Entity.Task;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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

    //find by id
    @Override
    public Optional<Task> findById(Long id) {

        String sql = """
            SELECT *
            FROM tasks
            WHERE id=?
            """;

        return jdbcTemplate.query(
                sql,
                taskRowMapper,
                id
        ).stream().findFirst();
    }

    //delete
    @Override
    public void delete(Long id) {

        String sql = """
            DELETE FROM tasks
            WHERE id=?
            """;

        jdbcTemplate.update(sql, id);
    }

    //update
    @Override
    public void update(Long id,
                       TaskUpdateRequest request) {

        String sql = """
            UPDATE tasks
            SET title=?,
                description=?,
                priority=?,
                status=?,
                due_date=?
            WHERE id=?
            """;

        jdbcTemplate.update(
                sql,
                request.title(),
                request.description(),
                request.priority().name(),
                request.status().name(),
                request.dueDate(),
                id
        );
    }

    //find by user
    @Override
    public List<Task> findByUserId(Long userId) {

        String sql = """
            SELECT *
            FROM tasks
            WHERE user_id=?
            """;

        return jdbcTemplate.query(
                sql,
                taskRowMapper,
                userId
        );
    }

    //find by status
    @Override
    public List<Task> findByStatus(Status status) {

        String sql = """
            SELECT *
            FROM tasks
            WHERE status=?
            """;

        return jdbcTemplate.query(
                sql,
                taskRowMapper,
                status.name()
        );
    }

    //find by Priority
    @Override
    public List<Task> findByPriority(Priority priority) {

        String sql = """
            SELECT *
            FROM tasks
            WHERE priority=?
            """;

        return jdbcTemplate.query(
                sql,
                taskRowMapper,
                priority.name()
        );
    }

    //get page wise
    @Override
    public List<Task> findAll(int page, int size) {

        int offset = page * size;

        String sql = """
            SELECT *
            FROM tasks
            LIMIT ?
            OFFSET ?
            """;

        return jdbcTemplate.query(
                sql,
                taskRowMapper,
                size,
                offset
        );
    }

    //get task by searching task title
    @Override
    public List<Task> searchByTitle(String keyword) {

        String sql = """
            SELECT *
            FROM tasks
            WHERE LOWER(title)
            LIKE LOWER(?)
            """;

        return jdbcTemplate.query(
                sql,
                taskRowMapper,
                "%" + keyword + "%"
        );
    }

    //sorting task
    @Override
    public List<Task> sortByDueDate() {

        String sql = """
            SELECT *
            FROM tasks
            ORDER BY due_date ASC
            """;

        return jdbcTemplate.query(
                sql,
                taskRowMapper
        );
    }

    //find overdue task
    @Override
    public List<Task> findOverdueTasks() {

        String sql = """
            SELECT *
            FROM tasks
            WHERE due_date < CURRENT_DATE
            AND status != 'COMPLETED'
            """;

        return jdbcTemplate.query(
                sql,
                taskRowMapper
        );
    }

}