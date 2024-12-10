package org.example.javaproj.model;

import java.time.Instant;

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

    //    TODO: Complete functionality
    public Board updatePixel(Board board, int x, int y, int val) {
        String matrix_data = board.getMatrixData();
        return board;
    }
}
