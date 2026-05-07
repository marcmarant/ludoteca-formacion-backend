package com.ccsw.tutorial.user;

import com.ccsw.tutorial.common.exception.AtLeastOneAdministratorRequiredException;
import com.ccsw.tutorial.user.model.CreateUserDTO;
import com.ccsw.tutorial.user.model.Role;
import com.ccsw.tutorial.user.model.User;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> findAll() {
        return (List<User>) this.userRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User findById(Long id) {
        Optional<User> user = this.userRepository.findById(id);

        if (user.isEmpty()) {
            throw new EntityNotFoundException();
        }
        return user.get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void create(CreateUserDTO dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        user.setRole(dto.getRole());

        this.userRepository.save(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateRole(Long id, Role newRole) {

        User user = this.userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Usuario " + id.toString() + " no encontrado")
        );

        if (!this.userRepository.existsByRoleAndIdNot(Role.ROLE_ADMIN, id)) {
            throw new AtLeastOneAdministratorRequiredException("Debe existir al menos un usuario con rol de administrador");
        }

        user.setRole(newRole);

        this.userRepository.save(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Long id) throws EntityNotFoundException {

        if (this.userRepository.findById(id).orElse(null) == null) {
            throw new EntityNotFoundException("Usuario " + id.toString() + " no econtrado");
        }

        if (!this.userRepository.existsByRoleAndIdNot(Role.ROLE_ADMIN, id)) {
            throw new AtLeastOneAdministratorRequiredException("Debe existir al menos un usuario con rol de administrador");
        }

        this.userRepository.deleteById(id);
    }
}
