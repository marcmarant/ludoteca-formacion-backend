package com.ccsw.tutorial.category;

import com.ccsw.tutorial.category.model.CategoryDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Categories", description = "API of Category")
@RequestMapping(value = "/categories")
@RestController
@CrossOrigin(origins = "*")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @Autowired
    ModelMapper mapper;

    @Operation(summary = "Find All", description = "Method that return a list of all categories")
    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<CategoryDTO> findAll() {
        return categoryService.findAll().stream().map(e -> mapper.map(e, CategoryDTO.class)).collect(Collectors.toList());
    }

    @Operation(summary = "Find by Id", description = "Method that return a category by id")
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public CategoryDTO findById(@PathVariable(name = "id") Long id) {
        return mapper.map(categoryService.findById(id), CategoryDTO.class);
    }

    @Operation(summary = "Create", description = "Method that creates a new category")
    @RequestMapping(path = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    // TODO: Ver si devolver el id de la categoría creada o el objeto completo
    public void create(@Valid @RequestBody CategoryDTO dto) {
        categoryService.create(dto);
    }

    @Operation(summary = "Update", description = "Method that updates an existing category")
    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable(name = "id") Long id, @Valid @RequestBody CategoryDTO dto) {
        dto.setId(id);
        categoryService.update(dto);
    }

    @Operation(summary = "Delete", description = "Method that deletes a category")
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(name = "id") Long id) {
        categoryService.delete(id);
    }
}
