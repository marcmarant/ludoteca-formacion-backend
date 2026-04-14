package com.ccsw.tutorial.user;

import com.ccsw.tutorial.user.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    /**
     * Recupera un usuario por su username
     *
     * @param username del usuario a recuperar
     * @return {@link User}
     */
    Optional<User> findByUsername(String username);
}
