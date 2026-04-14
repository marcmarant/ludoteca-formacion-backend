package com.ccsw.tutorial.user.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "users") // user es una palabra reservada en SQL
public class User {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    @NotBlank
    private String username;

    @Column(name = "password_hash", nullable = false)
    @NotBlank
    private String passwordHash;

    /**
     * @return id
     */
    public Long getId() {
        return this.id;
    }

    /**
     * @param id new value of {@link #getId}.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return name
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * @param username new value of {@link #getUsername}.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return passwordHash
     */
    public String getPasswordHash() {
        return this.passwordHash;
    }

    /**
     * @param passwordHash new value of {@link #getPasswordHash}.
     */
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    @Override
    public String toString() {
        return "ID: " + this.id + "\nUsername: " + this.username;
    }
}
