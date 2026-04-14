package com.ccsw.tutorial.user;

import com.ccsw.tutorial.user.model.User;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    public static final Long EXISTS_USER_ID = 1L;
    public static final Long NOT_EXISTS_USER_ID = 7L;

    @Test
    public void findByIdShouldReturnExpectedUser() {

        User mockUser = mock(User.class);

        when(userRepository.findById(EXISTS_USER_ID)).thenReturn(Optional.of(mockUser));

        User user = userService.findById(EXISTS_USER_ID);

        assertEquals(mockUser, user);
    }

    @Test
    public void findByNotExistingIdShouldThrowEntityNotFoundException() {

        when(userRepository.findById(NOT_EXISTS_USER_ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.findById(NOT_EXISTS_USER_ID));
    }

}