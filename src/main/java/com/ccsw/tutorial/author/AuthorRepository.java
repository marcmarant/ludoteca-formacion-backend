package com.ccsw.tutorial.author;

import com.ccsw.tutorial.author.model.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

/**
 * Repositorio para la entidad {@link Author}
 *
 * @author Marcos Martínez Antón
 */
public interface AuthorRepository extends CrudRepository<Author, Long> {

    /**
     * Recupera un listado paginado de {@link Author}
     *
     * @param pageable pageable
     * @return {@link Page} de {@link Author}
     */
    Page<Author> findAll(Pageable pageable);
}
