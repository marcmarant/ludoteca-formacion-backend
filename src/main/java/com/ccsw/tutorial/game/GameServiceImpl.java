package com.ccsw.tutorial.game;

import com.ccsw.tutorial.author.AuthorService;
import com.ccsw.tutorial.category.CategoryService;
import com.ccsw.tutorial.common.criteria.SearchCriteria;
import com.ccsw.tutorial.game.model.Game;
import com.ccsw.tutorial.game.model.GameDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class GameServiceImpl implements GameService {

    @Autowired
    GameRepository gameRepository;

    @Autowired
    AuthorService authorService;

    @Autowired
    CategoryService categoryService;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Game> find(String title, Long idCategory) {

        GameSpecification titleSpec = new GameSpecification(new SearchCriteria("title", ":", title));
        GameSpecification categorySpec = new GameSpecification(new SearchCriteria("category.id", ":", idCategory));
        Specification<Game> spec = titleSpec.and(categorySpec);

        return this.gameRepository.findAll(spec);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void create(GameDTO dto) {
        Game game = new Game();

        BeanUtils.copyProperties(dto, game, "author", "category");
        game.setAuthor(this.authorService.findById(dto.getAuthor().getId()));
        game.setCategory(this.categoryService.findById(dto.getCategory().getId()));

        this.gameRepository.save(game);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(GameDTO dto) {
        Optional<Game> optGame = this.gameRepository.findById(dto.getId());

        if (optGame.isEmpty()) {
            throw new EntityNotFoundException();
        }
        Game game = optGame.get();
        BeanUtils.copyProperties(dto, game, "id", "author", "category");
        game.setAuthor(this.authorService.findById(dto.getAuthor().getId()));
        game.setCategory(this.categoryService.findById(dto.getCategory().getId()));

        this.gameRepository.save(game);
    }

}
