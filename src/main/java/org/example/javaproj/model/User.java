package org.example.javaproj.model;

import java.time.Instant;


public class User {
    private Long id;

    private String username;

    private String email;

    private Instant dateCreated;

    // Constructors
    public User() {
    }

    public User(String username) {
        this.username = username;
//        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Instant getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }
}
