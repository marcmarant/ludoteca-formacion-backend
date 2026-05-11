package com.ccsw.tutorial.game;

import com.ccsw.tutorial.common.exception.DeleteEntityConflictException;
import com.ccsw.tutorial.game.model.Game;
import com.ccsw.tutorial.game.model.GameDTO;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

/**
 * Interfaz que define los servicios de games.
 * Permite hacer una busqueda filtrada, crear y actualizar.
 *
 * @author Marcos Martínez Antón
 */
public interface GameService {

    /**
     * Recupera los juegos filtrando opcionalmente por título y/o categoría
     *
     * @param title título del juego
     * @param author nombre del autor
     * @param idCategory PK de la categoría
     * @return {@link List} de {@link Game}
     */
    List<Game> find(String title, String author, Long idCategory);

    /**
     * Crea un nuevo juego
     *
     * @param dto datos de la entidad
     */
    void create(GameDTO dto);

    /**
     * Actualizar una juego
     *
     * @param dto con los nuevos datos de la entidad incluyendo su id
     * @throws EntityNotFoundException si no existe la categoría a actualizar
     */
    void update(GameDTO dto) throws EntityNotFoundException;

}
