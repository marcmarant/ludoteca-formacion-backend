package com.ccsw.tutorial.author;

import com.ccsw.tutorial.author.model.Author;
import com.ccsw.tutorial.author.model.AuthorDTO;
import com.ccsw.tutorial.author.model.AuthorSearchDTO;
import com.ccsw.tutorial.common.exception.DeleteEntityConflictException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AuthorService {

    /**
     * Recupera un listado de autores {@link Author}
     *
     * @return {@link List} de {@link Author}
     */
    List<Author> findAll();

    /**
     * Recupera una página de autores
     *
     * @param dto filtros y paginación
     * @return {@link Page} de {@link Author}
     */
    Page<Author> findPage(AuthorSearchDTO dto);

    /**
     * Recupera un autor en concreto
     *
     * @param id PK de la entidad
     * @throws EntityNotFoundException si no existe el autor a recuperar
     * @return {@link Author}
     */
    Author findById(Long id) throws EntityNotFoundException;

    /**
     * Crea un nuevo autor
     *
     * @param dto datos de la entidad
     */
    void create(AuthorDTO dto);

    /**
     * Actualiza un autor existente
     *
     * @param dto con los nuevos datos de la entidad incluyendo su id
     * @throws EntityNotFoundException si no existe el autor a actualizar
     */
    void update(AuthorDTO dto) throws EntityNotFoundException;

    /**
     * Elimina un autor existente
     *
     * @param id PK de la entidad
     * @throws EntityNotFoundException si no existe el autor a borrar
     * @throws DeleteEntityConflictException si existe un juego asociado a este autor
     */
    void delete(Long id) throws EntityNotFoundException, DeleteEntityConflictException;

}
