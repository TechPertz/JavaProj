package org.example.javaproj.backend.model;

import java.time.Instant;
import org.example.javaproj.backend.Constants;

public class Board {
    private Long id;

    private Long ownerId;

    private String matrixData;

    private Instant dateCreated;

    public Board() {
    }

    public Board(User owner) {
        this.ownerId = owner.getId();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getMatrixData() {
        return matrixData;
    }

    public void setMatrixData(String matrixData) {
        this.matrixData = matrixData;
    }

    public Instant getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void updatePixel(Board board, int x, int y, int val) {
        int index = (x * Constants.RESOLUTION_WIDTH) + y;

        if (index < 0 || index >= board.getMatrixData().length()) {
            throw new IllegalArgumentException("Invalid x or y coordinates.");
        }

        char[] matrixArray = board.getMatrixData().toCharArray();
        matrixArray[index] = (char) ('0' + val); // Convert integer val to char ('0' or '1')
        board.setMatrixData(new String(matrixArray));
    }
}
