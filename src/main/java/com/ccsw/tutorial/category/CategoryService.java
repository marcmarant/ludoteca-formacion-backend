package com.ccsw.tutorial.category;

import com.ccsw.tutorial.category.model.Category;
import com.ccsw.tutorial.category.model.CategoryDTO;
import com.ccsw.tutorial.common.exception.DeleteEntityConflictException;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

public interface CategoryService {

    /**
     * Recuperar todas las categorías
     *
     * @return {@link List} de {@link Category}
     */
    List<Category> findAll();

    /**
     * Recupera una categoria concreta
     *
     * @param id PK de la entidad
     * @throws EntityNotFoundException si no existe la categoría a recuperar
     * @return {@link Category}
     */
    Category findById(Long id) throws EntityNotFoundException;

    /**
     * Crea una nueva categoría
     *
     * @param dto datos de la entidad
     */
    void create(CategoryDTO dto);

    /**
     * Actualiza una categoría existente
     *
     * @param dto con los nuevos datos de la entidad incluyendo su id
     * @throws EntityNotFoundException si no existe la categoría a actualizar
     */
    void update(CategoryDTO dto) throws EntityNotFoundException;

    /**
     * Elimina una categoría
     *
     * @param id PK de la entidad
     * @throws EntityNotFoundException si no existe la categoría a borrar
     * @throws DeleteEntityConflictException si existe una categoria asociada a este juego
     */
    void delete(Long id) throws EntityNotFoundException;

}
