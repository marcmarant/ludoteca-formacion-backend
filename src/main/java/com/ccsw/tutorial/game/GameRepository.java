package com.ccsw.tutorial.game;

import com.ccsw.tutorial.author.model.Author;
import com.ccsw.tutorial.game.model.Game;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GameRepository extends CrudRepository<Game, Long>, JpaSpecificationExecutor<Game> {

    /**
     * Recupera un listado filtrado de {@link Game}
     *
     * @param spec especificacion con los filtros de busqueda
     * @return {@link List} de {@link Author}
     */
    @Override
    @EntityGraph(attributePaths = {"category", "author"})
    List<Game> findAll(Specification<Game> spec);

    /**
     * Devuelve si existe algun juego asociado a un autor concreto
     *
     * @param authorId id del autor
     * @return true si existe algun juego asociado al autor, false en caso contrario
     */
    boolean existsByAuthor_Id(Long authorId);
}
