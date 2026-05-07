package com.ccsw.tutorial.user;

import com.ccsw.tutorial.user.model.CreateUserDTO;
import com.ccsw.tutorial.user.model.Role;
import com.ccsw.tutorial.user.model.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Users", description = "API of User")
@RequestMapping(value = "/users")
@RestController
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    ModelMapper mapper;

    @Operation(summary = "Find All", description = "Method that return a list of all users")
    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<UserDTO> findAll() {
        return userService.findAll().stream().map(e -> mapper.map(e, UserDTO.class)).collect(Collectors.toList());
    }

    @Operation(summary = "Find by Id", description = "Method that return a user by id")
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public UserDTO findById(@PathVariable(name = "id") Long id) {
        return mapper.map(userService.findById(id), UserDTO.class);
    }

    @Operation(summary = "Create", description = "Method that creates a new user")
    @RequestMapping(path = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@Valid @RequestBody CreateUserDTO dto) {
        userService.create(dto);
    }

    @Operation(summary = "Update Role", description = "Method that updates the role of a user")
    @RequestMapping(path = "/{id}", method = RequestMethod.PATCH)
    public void update(@PathVariable(name = "id") Long id, @RequestParam Role role) {
        userService.updateRole(id, role);
    }

    @Operation(summary = "Delete", description = "Method that deletes a user")
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(name = "id") Long id) {
        userService.delete(id);
    }
}