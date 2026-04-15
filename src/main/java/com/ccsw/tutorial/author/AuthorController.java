package com.ccsw.tutorial.author;

import com.ccsw.tutorial.author.model.Author;
import com.ccsw.tutorial.author.model.AuthorDTO;
import com.ccsw.tutorial.author.model.AuthorSearchDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Authors", description = "API of Author")
@RequestMapping(value = "/authors")
@RestController
@CrossOrigin(origins = "*")
public class AuthorController {

    @Autowired
    AuthorService authorService;

    @Autowired
    ModelMapper mapper;

    @Operation(summary = "Find All", description = "Method that returns all authors.")
    @RequestMapping(path = "", method = RequestMethod.GET)
    public Object findAll() {
        List<Author> authors = authorService.findAll();
        return authors.stream().map(e -> mapper.map(e, AuthorDTO.class)).collect(Collectors.toList());
    }

    @Operation(summary = "Find a Page", description = "Method that returns a page of authors")
    @RequestMapping(path = "/search", method = RequestMethod.POST)
    public Object findPage(@Valid @RequestBody AuthorSearchDTO dto) {
        return authorService.findPage(dto).map(e -> mapper.map(e, AuthorDTO.class));
    }

    @Operation(summary = "Find by Id", description = "Method that return an author by id")
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public AuthorDTO findById(@PathVariable(name = "id") Long id) {
        return mapper.map(authorService.findById(id), AuthorDTO.class);
    }

    @Operation(summary = "Create", description = "Method that creates a new author")
    @RequestMapping(path = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@Valid @RequestBody AuthorDTO dto) {
        authorService.create(dto);
    }

    @Operation(summary = "Update", description = "Method that updates an existing author")
    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable(name = "id") Long id, @Valid @RequestBody AuthorDTO dto) {
        dto.setId(id);
        authorService.update(dto);
    }

    @Operation(summary = "Delete", description = "Method that deletes an author")
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(name = "id") Long id) {
        authorService.delete(id);
    }
}
