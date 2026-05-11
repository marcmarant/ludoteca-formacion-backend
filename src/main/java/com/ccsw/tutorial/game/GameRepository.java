package com.ccsw.tutorial.game;

import com.ccsw.tutorial.game.model.Game;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Repositorio para la entidad {@link Game}
 *
 * @author Marcos Martínez Antón
 */
public interface GameRepository extends CrudRepository<Game, Long>, JpaSpecificationExecutor<Game> {

    /**
     * Recupera un listado filtrado de {@link Game}
     *
     * @param spec especificacion con los filtros de busqueda
     * @return {@link List} de {@link Game}
     */
    @Override
    @EntityGraph(attributePaths = {"category", "author"})
    List<Game> findAll(Specification<Game> spec);

    /**
     * Devuelve si existe algun juego asociado a una categoria en concreto
     *
     * @param categoryId id de la categoria
     * @return true si existe algun juego asociado a la categoria, false en caso contrario
     */
    boolean existsByCategoryId(Long categoryId);

    /**
     * Devuelve si existe algun juego asociado a un autor concreto
     *
     * @param authorId id del autor
     * @return true si existe algun juego asociado al autor, false en caso contrario
     */
    boolean existsByAuthorId(Long authorId);
}
