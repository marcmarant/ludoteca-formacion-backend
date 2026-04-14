package com.ccsw.tutorial.user;

import com.ccsw.tutorial.user.model.User;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

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
}
