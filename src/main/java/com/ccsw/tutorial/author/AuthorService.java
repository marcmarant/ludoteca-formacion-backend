package com.ccsw.tutorial.author;

import com.ccsw.tutorial.author.model.Author;
import com.ccsw.tutorial.author.model.AuthorDTO;
import com.ccsw.tutorial.author.model.AuthorSearchDTO;
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
     * Método para recuperar una pagina de autores
     *
     * @return {@link Page} de {@link Author}
     */
    Page<Author> findPage(AuthorSearchDTO dto);

    /**
     * Método para recuperar un autor concreta
     *
     * @param id PK de la entidad
     * @throws EntityNotFoundException si no existe el autor a recuperar
     * @return {@link Author}
     */
    Author findById(Long id) throws EntityNotFoundException;

    /**
     * Método para crear  un autor
     *
     * @param dto datos de la entidad
     */
    void create(AuthorDTO dto);

    /**
     * Método para actualizar un autor
     *
     * @param dto con los nuevos datos de la entidad incluyendo su id
     * @throws EntityNotFoundException si no existe el autor a actualizar
     */
    void update(AuthorDTO dto) throws EntityNotFoundException;

    /**
     * Método para borrar un autor
     *
     * @param id PK de la entidad
     * @throws EntityNotFoundException si no existe el autor a borrar
     */
    void delete(Long id) throws EntityNotFoundException;

}
