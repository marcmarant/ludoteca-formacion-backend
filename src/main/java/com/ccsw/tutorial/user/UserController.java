package com.ccsw.tutorial.user;

import com.ccsw.tutorial.user.model.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Users", description = "API of User")
@RequestMapping(value = "/users")
@RestController
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    ModelMapper mapper;

    @Operation(summary = "Find by Id", description = "Method that return a user by id")
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public UserDTO findById(@PathVariable(name = "id") Long id) {
        return mapper.map(userService.findById(id), UserDTO.class);
    }
}