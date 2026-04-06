package com.ccsw.tutorial.game;

import com.ccsw.tutorial.game.model.Game;
import com.ccsw.tutorial.game.model.GameDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Game", description = "API of Game")
@RequestMapping(value = "/games")
@RestController
@CrossOrigin(origins = "*")
public class GameController {

    @Autowired
    GameService gameService;

    @Autowired
    ModelMapper mapper;

    @Operation(summary = "Find", description = "Method that return a filtered list of Games")
    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<GameDTO> find(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "idCategory", required = false) Long idCategory
    ) {
        List<Game> games = gameService.find(title, idCategory);

        return games.stream().map(e -> mapper.map(e, GameDTO.class)).collect(Collectors.toList());
    }

    @Operation(summary = "Create", description = "Method that creates a new game")
    @RequestMapping(path = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@Valid @RequestBody GameDTO dto) {
        gameService.create(dto);
    }

    @Operation(summary = "Update", description = "Method that updates an existing game")
    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable(name = "id") Long id, @Valid @RequestBody GameDTO dto) {
        dto.setId(id);
        gameService.update(dto);
    }

}
