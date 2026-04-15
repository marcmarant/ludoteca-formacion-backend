package com.ccsw.tutorial.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication", description = "API for authentication")
@RequestMapping(value = "/auth")
@RestController
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Operation(summary = "Authenticate User", description = "Method that returns a JWT token if the user is authenticated")
    @RequestMapping(path = {"", "/"}, method = RequestMethod.POST)
    public String auth(@Valid @RequestBody AuthDTO request) {
        return authService.auth(request);
    }
}