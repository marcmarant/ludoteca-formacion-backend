package com.ccsw.tutorial.category;

import com.ccsw.tutorial.category.model.Category;
import com.ccsw.tutorial.category.model.CategoryDTO;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

public interface CategoryService {

    /**
     * Método para recuperar todas las categorías
     *
     * @return {@link List} de {@link Category}
     */
    List<Category> findAll();

    /**
     * Método para recuperar una categoria concreta
     *
     * @param id PK de la entidad
     * @throws EntityNotFoundException si no existe la categoría a recuperar
     * @return {@link Category}
     */
    Category findById(Long id) throws EntityNotFoundException;

    /**
     * Método para crear  una categoría
     *
     * @param dto datos de la entidad
     */
    void create(CategoryDTO dto);

    /**
     * Método para actualizar una categoría
     *
     * @param dto con los nuevos datos de la entidad incluyendo su id
     * @throws EntityNotFoundException si no existe la categoría a actualizar
     */
    void update(CategoryDTO dto) throws EntityNotFoundException;

    /**
     * Método para borrar una categoría
     *
     * @param id PK de la entidad
     * @throws EntityNotFoundException si no existe la categoría a borrar
     */
    void delete(Long id) throws EntityNotFoundException;

}
