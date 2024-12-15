package org.example.javaproj.backend.dto;

public class LoginResponse {
    private String message;
    private Long user_id;
    private long board_id;
    private String board_matrix_data;

    public LoginResponse() {

    }

    public LoginResponse(String message, Long user_id, long board_id, String board_matrix_data) {
        this.message = message;
        this.user_id = user_id;
        this.board_id = board_id;
        this.board_matrix_data = board_matrix_data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public long getBoard_id() {
        return board_id;
    }

    public void setBoard_id(long board_id) {
        this.board_id = board_id;
    }

    public String getBoard_matrix_data() {
        return board_matrix_data;
    }

    public void setBoard_matrix_data(String board_matrix_data) {
        this.board_matrix_data = board_matrix_data;
    }
}
