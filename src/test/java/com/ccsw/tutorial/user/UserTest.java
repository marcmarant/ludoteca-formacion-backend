package com.ccsw.tutorial.user;

import com.ccsw.tutorial.common.exception.AtLeastOneAdministratorRequiredException;
import com.ccsw.tutorial.user.model.CreateUserDTO;
import com.ccsw.tutorial.user.model.Role;
import com.ccsw.tutorial.user.model.User;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    public static final Long EXISTS_USER_ID = 1L;
    public static final Long NOT_EXISTS_USER_ID = 7L;

    @Test
    public void findAllShouldReturnAllUsers() {

        List<User> list = new ArrayList<>();
        list.add(mock(User.class));

        when(userRepository.findAll()).thenReturn(list);

        List<User> users = userService.findAll();

        assertNotNull(users);
        assertEquals(1, users.size());
    }

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

    @Test
    public void createShouldCreateAValidUser() {

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(EXISTS_USER_ID);
            return user;
        });

        when(passwordEncoder.encode(anyString())).thenAnswer(invocation -> "encoded_" + invocation.getArgument(0));

        CreateUserDTO dto = new CreateUserDTO();
        dto.setUsername("testusername");
        dto.setPassword("testpassword");
        dto.setRole(Role.ROLE_EMPLOYEE);
        userService.create(dto);

        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();

        assertEquals(EXISTS_USER_ID, savedUser.getId());
        assertEquals(dto.getUsername(), savedUser.getUsername());
        assertEquals("encoded_" + dto.getPassword(), savedUser.getPasswordHash());
        assertEquals(dto.getRole(), savedUser.getRole());
    }

    @Test
    public void updateRoleShouldUpdateAUserRole() {

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        User existingUser = new User();
        existingUser.setId(EXISTS_USER_ID);
        existingUser.setUsername("testusernmae");
        existingUser.setRole(Role.ROLE_EMPLOYEE);

        when(userRepository.findById(EXISTS_USER_ID)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByRoleAndIdNot(Role.ROLE_ADMIN, EXISTS_USER_ID)).thenReturn(true);

        userService.updateRole(EXISTS_USER_ID, Role.ROLE_ADMIN);

        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();

        assertEquals(EXISTS_USER_ID, savedUser.getId());
        assertEquals("testusernmae", savedUser.getUsername());
        assertEquals(Role.ROLE_ADMIN, savedUser.getRole());
    }

    @Test
    public void updateLastAdminRoleShouldThrowAtLeastOneAdministratorRequiredException() {

        User mockUser = mock(User.class);

        when(userRepository.findById(EXISTS_USER_ID)).thenReturn(Optional.of(mockUser));
        when(userRepository.existsByRoleAndIdNot(Role.ROLE_ADMIN, EXISTS_USER_ID)).thenReturn(false);

        assertThrows(
                AtLeastOneAdministratorRequiredException.class,
                () -> userService.updateRole(EXISTS_USER_ID, Role.ROLE_EMPLOYEE)
        );

        verify(userRepository, never()).save(any(User.class));
    }


    @Test
    public void deleteShouldDeleteExpectedUser() {

        User mockUser = mock(User.class);

        when(userRepository.findById(EXISTS_USER_ID)).thenReturn(Optional.of(mockUser));
        when(userRepository.existsByRoleAndIdNot(Role.ROLE_ADMIN, EXISTS_USER_ID)).thenReturn(true);

        userService.delete(EXISTS_USER_ID);

        verify(userRepository).deleteById(EXISTS_USER_ID);
    }

    @Test
    public void deleteNonExistentUserShouldThrowEntityNotFoundException() {

        when(userRepository.findById(NOT_EXISTS_USER_ID)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> userService.delete(NOT_EXISTS_USER_ID));

        verify(userRepository, never()).deleteById(anyLong());
    }

    @Test
    public void deleteLastAdminUserShouldThrowAtLeastOneAdministratorRequiredException() {

        User mockUser = mock(User.class);

        when(userRepository.findById(EXISTS_USER_ID)).thenReturn(Optional.of(mockUser));
        when(userRepository.existsByRoleAndIdNot(Role.ROLE_ADMIN, EXISTS_USER_ID)).thenReturn(false);

        assertThrows(AtLeastOneAdministratorRequiredException.class, () -> userService.delete(EXISTS_USER_ID));
        verify(userRepository, never()).deleteById(anyLong());
    }

}