package com.example.forum_api.service;

import com.example.forum_api.DTO.UserDTO;
import com.example.forum_api.Repository.UserRepository;
import com.example.forum_api.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    /**
     *  Testtyp: Enhetstest
     * Testar att en ny användare skapas när användarnamn och e-post är unika.
     */
    @Test
    void createUser_shouldSaveUserWhenUsernameAndEmailAreUnique() {
        User user = new User();
        user.setUsername("john");
        user.setEmail("john@example.com");
        user.setPassword("secret");

        when(userRepository.findByUsername("john")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.empty());
        when(userRepository.save(user)).thenReturn(user);

        UserDTO result = userService.createUser(user);

        assertEquals("john", result.getUsername());
        assertEquals("john@example.com", result.getEmail());
        verify(userRepository).save(user);
    }

    /**
     * ✅ Testtyp: Enhetstest
     * Testar att ett undantag kastas när användaren inte hittas via användarnamn.
     */
    @Test
    void getUserByUsername_shouldThrowExceptionWhenUserNotFound() {
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.getUserByUsername("nonexistent");
        });

        assertTrue(exception.getMessage().contains("not found"));
    }
}
