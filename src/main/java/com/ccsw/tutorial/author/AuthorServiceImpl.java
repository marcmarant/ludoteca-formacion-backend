package com.ccsw.tutorial.author;

import com.ccsw.tutorial.author.model.Author;
import com.ccsw.tutorial.author.model.AuthorDTO;
import com.ccsw.tutorial.author.model.AuthorSearchDTO;
import com.ccsw.tutorial.common.exception.DeleteEntityConflictException;
import com.ccsw.tutorial.game.GameRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementación de los servicios de autores.
 *
 * @author Marcos Martínez Antón
 */
@Service
@Transactional
public class AuthorServiceImpl implements AuthorService {

    @Autowired
    AuthorRepository authorRepository;

    @Autowired
    GameRepository gameRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Author> findAll() {
        List<Author> authors = (List<Author>) this.authorRepository.findAll();

        authors.forEach(author ->
                author.setHasGames(gameRepository.existsByAuthorId(author.getId()))
        );
        return authors;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<Author> findPage(AuthorSearchDTO dto) {
        Page<Author> authors = this.authorRepository.findAll(dto.getPageable().getPageable());

        authors.forEach(author ->
                author.setHasGames(gameRepository.existsByAuthorId(author.getId()))
        );
        return authors;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Author findById(Long id) throws EntityNotFoundException {

        Author author = this.authorRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        author.setHasGames(gameRepository.existsByAuthorId(author.getId()));

        return author;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void create(AuthorDTO dto) {

        Author category = new Author();
        category.setName(dto.getName());
        category.setNationality(dto.getNationality());

        this.authorRepository.save(category);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(AuthorDTO dto) {

        Optional<Author> optAuthor = this.authorRepository.findById(dto.getId());

        if (optAuthor.isEmpty()) {
            throw new EntityNotFoundException();
        }
        Author author = optAuthor.get();
        author.setName(dto.getName());
        author.setNationality(dto.getNationality());

        this.authorRepository.save(author);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Long id) throws EntityNotFoundException {

        if (this.authorRepository.findById(id).orElse(null) == null) {
            throw new EntityNotFoundException("Autor " + id + " no encontrado");
        }
        if (this.gameRepository.existsByAuthorId(id)) {
            throw new DeleteEntityConflictException("No se puede borrar el autor " + id + " porque esta asociado a un juego");
        }
        this.authorRepository.deleteById(id);
    }

}
