package org.example.javaproj.backend.service;

import org.apache.logging.log4j.LogManager;
import org.example.javaproj.backend.model.Board;
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
public class BoardService {
    private static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger();
    private final JdbcTemplate jdbcTemplate;

    public BoardService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Board createBoard(Board board) {
        String sql = "INSERT INTO boards (owner_id, matrix_data, date_created) VALUES (?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, board.getOwnerId());
            ps.setString(2, board.getMatrixData());
            ps.setTimestamp(3, Timestamp.from(Instant.now()));
            return ps;
        }, keyHolder);

        Map<String, Object> keys = keyHolder.getKeys();
        assert keys != null;
        long generatedId = ((Number) keys.get("id")).longValue();
        board.setId(generatedId);
        return board;
    }

    public Board getOrCreateMainBoard(User owner) {
        String sql = "SELECT * FROM boards";
        try {
            return jdbcTemplate.queryForObject(sql, this::mapRowToBoard);
        } catch (EmptyResultDataAccessException e) {
            Board newBoard = new Board(owner);
            return createBoard(newBoard);
        }
    }

    public Board getMainBoard() {
        String sql = "SELECT * FROM boards";
        return jdbcTemplate.queryForObject(sql, this::mapRowToBoard);
    }

    public Board getBoardById(Long board_id) {
        String sql = "SELECT * FROM boards WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, this::mapRowToBoard, board_id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    private Board mapRowToBoard(ResultSet rs, int rowNum) throws SQLException {
        Board board = new Board();
        board.setId(rs.getLong("id"));
        board.setOwnerId(rs.getLong("owner_id"));
        board.setMatrixData(rs.getString("matrix_data"));
        board.setDateCreated(rs.getTimestamp("date_created").toInstant());
        return board;
    }

    public void updateMatrixBoard(Board board) {
        String sql = "UPDATE boards SET matrix_data = ? WHERE id = ?";
        jdbcTemplate.update(sql, board.getMatrixData(), board.getId());
    }
}
