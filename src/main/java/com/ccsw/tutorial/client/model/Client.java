package com.ccsw.tutorial.client.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

/**
 * Entidad cliente, que representa un cliente al que le es prestado un juego de mesa.
 *
 * @author Marcos Martínez Antón
 */
@Entity
@Table(name = "client")
public class Client {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    @NotBlank
    private String name;

    @Transient
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

    @Override
    public String toString() {
        return "ID: " + this.id + "\nName: " + this.name;
    }
}
