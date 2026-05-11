package com.ccsw.tutorial.user.model;

/**
 * DTO para crear un nuevo usuario.
 *
 * @author Marcos Martínez Antón
 */
public class CreateUserDTO {

    private String username;
    private String password;
    private Role role;

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
     * @return id
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * @param password new value of {@link #getPassword}.
     */
    public void setPassword(String password) {
        this.password = password;
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
