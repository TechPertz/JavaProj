package org.example.javaproj.model;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String username;

//    @Column(nullable=true)
//    private String email;

    @Column(name = "date_created", nullable=false)
    private Instant dateCreated;

    // Constructors
    public User() {}

    public User(String username) {
        this.username = username;
//        this.email = email;
    }

    @PrePersist
    protected void onCreate() {
        this.dateCreated = Instant.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
