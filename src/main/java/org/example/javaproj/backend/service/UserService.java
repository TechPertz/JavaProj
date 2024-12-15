package org.example.javaproj.backend.service;

import org.example.javaproj.backend.model.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.time.Instant;
import java.util.Map;


@Service
public class UserService {

    private final JdbcTemplate jdbcTemplate;

    public UserService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public User createUser(User user) {
        String sql = "INSERT INTO users (email, username, date_created) VALUES (?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getUsername());
            ps.setTimestamp(3, Timestamp.from(Instant.now()));
            return ps;
        }, keyHolder);

        Map<String, Object> keys = keyHolder.getKeys();
        long generatedId = ((Number) keys.get("id")).longValue();
        user.setId(generatedId);
        return user;
    }

    public User getUserByUsername(String username) {
        String sql = "SELECT id, email, username, date_created FROM users WHERE username = ?";
        try {
            return jdbcTemplate.queryForObject(sql, this::mapRowToUser, username);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    private User mapRowToUser(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setEmail(rs.getString("email"));
        user.setUsername(rs.getString("username"));
        user.setDateCreated(rs.getTimestamp("date_created").toInstant());
        return user;
    }

}
