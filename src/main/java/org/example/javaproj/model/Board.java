package org.example.javaproj.model;

import java.time.Instant;

public class Board {
    private Long id;

    private User owner;

    private boolean[][] matrixData;

    private Instant dateCreated;

    public Board() {
    }

    public Board(User owner) {
        this.owner = owner;
    }

}
