package com.ccsw.tutorial.auth;

import jakarta.validation.constraints.NotBlank;

public class AuthDTO {

    @NotBlank(message = "El nombre de usuario no puede estar vacío")
    private String username;

    @NotBlank(message = "La contraseña no puede estar vacia")
    private String password;

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
     * @return password
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
}