package com.ccsw.tutorial.author;

import com.ccsw.tutorial.author.model.Author;
import com.ccsw.tutorial.author.model.AuthorDTO;
import com.ccsw.tutorial.author.model.AuthorSearchDTO;
import com.ccsw.tutorial.common.pagination.PageableRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

    @Operation(summary = "Find", description = "Method that returns a list of authors. If page params are provided it returns a page of authors.")
    @RequestMapping(path = "", method = RequestMethod.GET)
    public Object find(
            @RequestParam(required = false) Integer pageNumber,
            @RequestParam(required = false) Integer pageSize
    ) {
        // Si no se pasan parametros de paginación se devuelven todos los autores
        if (pageNumber == null || pageSize == null) {
            List<Author> authors = authorService.findAll();
            return authors.stream().map(e -> mapper.map(e, AuthorDTO.class)).collect(Collectors.toList());
        }

        AuthorSearchDTO dto = new AuthorSearchDTO();
        dto.setPageable(new PageableRequest(pageNumber, pageSize));

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
