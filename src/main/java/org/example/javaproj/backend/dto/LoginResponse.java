package org.example.javaproj.backend.dto;

import org.example.javaproj.backend.Constants;

public class LoginResponse {
    private String message;
    private Long user_id;
    private long board_id;
    private short[][] board_matrix_data;


    public LoginResponse() {

    }

    public LoginResponse(String message, Long user_id, long board_id, String board_matrix_data) {
        this.message = message;
        this.user_id = user_id;
        this.board_id = board_id;
        this.board_matrix_data = getMatrixFromBoardString(board_matrix_data);
    }

    public void printMatrixBoardCount(short[][] matrixArray){
        int count = 0;
        for (short[] ints : matrixArray) {
            for (short anInt : ints) {
                if (anInt != 0) {
                    count++;
                }
            }
        }
        System.out.println("Matrix board Pen Mark count = " + count);
    }

    public short[][] getMatrixFromBoardString(String boardMatrixString) {
        short[][] resp = new short[Constants.RESOLUTION_HEIGHT][Constants.RESOLUTION_WIDTH];
        for (int i = 0; i < Constants.RESOLUTION_HEIGHT; i++) {
            for (int j = 0; j < Constants.RESOLUTION_WIDTH; j++) {
                resp[i][j] = (short) (boardMatrixString.charAt(i * Constants.RESOLUTION_WIDTH + j) - '0');
            }
        }
        printMatrixBoardCount(resp);
        return resp;
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

    public short[][] getBoard_matrix_data() {
        return board_matrix_data;
    }

    public void setBoard_matrix_data(short[][] board_matrix_data) {
        this.board_matrix_data = board_matrix_data;
    }
}
