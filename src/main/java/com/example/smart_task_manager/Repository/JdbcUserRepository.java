package com.example.smart_task_manager.Repository;

import com.example.smart_task_manager.Dto.UserRequest;
import com.example.smart_task_manager.Entity.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JdbcUserRepository implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(UserRequest request) {

        String sql = """
                INSERT INTO users(name,email,password)
                VALUES(?,?,?)
                """;

        jdbcTemplate.update(
                sql,
                request.name(),
                request.email(),
                request.password()
        );
    }

    @Override
    public List<User> findAll() {

        String sql = """
                SELECT *
                FROM users
                ORDER BY id
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {

            User user = new User();

            user.setId(rs.getLong("id"));
            user.setName(rs.getString("name"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

            return user;
        });

    }
    
    @Override
    public Optional<User> findById(Long id) {

        String sql = """
            SELECT *
            FROM users
            WHERE id = ?
            """;

        List<User> users = jdbcTemplate.query(sql,
                (rs, rowNum) -> {

                    User user = new User();

                    user.setId(rs.getLong("id"));
                    user.setName(rs.getString("name"));
                    user.setEmail(rs.getString("email"));
                    user.setPassword(rs.getString("password"));
                    user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

                    return user;

                }, id);

        return users.stream().findFirst();
    }

    @Override
    public void delete(Long id) {
        String sql = """
            DELETE FROM users
            WHERE id = ?
            """;

        jdbcTemplate.update(sql, id);
    }
}