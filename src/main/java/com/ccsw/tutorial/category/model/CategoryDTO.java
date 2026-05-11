package com.ccsw.tutorial.category.model;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO de categoria para comunicar el controlador con los servicios.
 *
 * @author Marcos Martínez Antón
 */
public class CategoryDTO {

    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío")
    private String name;

    private boolean hasGames;

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
    public String getName() {
        return this.name;
    }

    /**
     * @param name new value of {@link #getName}.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return hasGames
     */
    public boolean getHasGames() {
        return this.hasGames;
    }

    /**
     * @param hasGames new value of {@link #getHasGames}.
     */
    public void setHasGames(boolean hasGames) {
        this.hasGames = hasGames;
    }
}