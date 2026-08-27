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

        if (rs.getDate("due_date") != null) {
            task.setDueDate(rs.getDate("due_date").toLocalDate());
        }

        task.setUserId(rs.getLong("user_id"));

        if (rs.getTimestamp("created_at") != null) {
            task.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        }

        try {
            task.setUserEmail(rs.getString("user_email"));
        } catch (Exception ignored) {
        }

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
                SELECT t.*, u.email AS user_email
                FROM tasks t
                LEFT JOIN users u ON t.user_id = u.id
                ORDER BY t.id
                """;

        return jdbcTemplate.query(sql, taskRowMapper);
    }

    //find by id
    @Override
    public Optional<Task> findById(Long id) {

        String sql = """
            SELECT t.*, u.email AS user_email
            FROM tasks t
            LEFT JOIN users u ON t.user_id = u.id
            WHERE t.id=?
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

        if (request.userId() != null) {
            String sql = """
                UPDATE tasks
                SET title=?,
                    description=?,
                    priority=?,
                    status=?,
                    due_date=?,
                    user_id=?
                WHERE id=?
                """;

            jdbcTemplate.update(
                    sql,
                    request.title(),
                    request.description(),
                    request.priority().name(),
                    request.status().name(),
                    request.dueDate(),
                    request.userId(),
                    id
            );
        } else {
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
    }

    //find by user
    @Override
    public List<Task> findByUserId(Long userId) {

        String sql = """
            SELECT t.*, u.email AS user_email
            FROM tasks t
            LEFT JOIN users u ON t.user_id = u.id
            WHERE t.user_id=?
            ORDER BY t.id
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
            SELECT t.*, u.email AS user_email
            FROM tasks t
            LEFT JOIN users u ON t.user_id = u.id
            WHERE t.status=?
            ORDER BY t.id
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
            SELECT t.*, u.email AS user_email
            FROM tasks t
            LEFT JOIN users u ON t.user_id = u.id
            WHERE t.priority=?
            ORDER BY t.id
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
            SELECT t.*, u.email AS user_email
            FROM tasks t
            LEFT JOIN users u ON t.user_id = u.id
            ORDER BY t.id
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
            SELECT t.*, u.email AS user_email
            FROM tasks t
            LEFT JOIN users u ON t.user_id = u.id
            WHERE LOWER(t.title)
            LIKE LOWER(?)
            ORDER BY t.id
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
            SELECT t.*, u.email AS user_email
            FROM tasks t
            LEFT JOIN users u ON t.user_id = u.id
            ORDER BY t.due_date ASC
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
            SELECT t.*, u.email AS user_email
            FROM tasks t
            LEFT JOIN users u ON t.user_id = u.id
            WHERE t.due_date < CURRENT_DATE
            AND t.status != 'COMPLETED'
            ORDER BY t.id
            """;

        return jdbcTemplate.query(
                sql,
                taskRowMapper
        );
    }

}