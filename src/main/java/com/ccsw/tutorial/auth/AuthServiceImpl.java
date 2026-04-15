package com.ccsw.tutorial.auth;

import com.ccsw.tutorial.common.exception.UnauthorizedException;
import com.ccsw.tutorial.user.UserRepository;
import com.ccsw.tutorial.user.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public String auth(AuthDTO request) {
        Optional<User> user = this.userRepository.findByUsername(request.getUsername());

        if (user.isPresent() && this.passwordEncoder.matches(request.getPassword(), user.get().getPasswordHash())) {
            return this.jwtService.createToken(user.get());
        } else {
            throw new UnauthorizedException("Usuario o contraseña incorrectos");
        }
    }
}
