package com.ccsw.tutorial.user.model;

/**
 * DTO de usuario para comunicar el controlador con los servicios.
 *
 * @author Marcos Martínez Antón
 */
public class UserDTO {

    private Long id;
    private String username;
    private Role role;

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
     * @return username
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
     * @return role
     */
    public Role getRole() {
        return this.role;
    }

    /**
     * @param role new value of {@link #getRole}.
     */
    public void setRole(Role role) {
        this.role = role;
    }
}