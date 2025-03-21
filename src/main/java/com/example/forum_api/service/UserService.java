package com.example.forum_api.service;

import com.example.forum_api.models.User;
import com.example.forum_api.DTO.UserDTO;
import com.example.forum_api.models.User;
import com.example.forum_api.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Hämta alla användare
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::toUserDTO)
                .collect(Collectors.toList());
    }

    // Hämta en enskild användare via ID
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User with ID " + id + " not found."));
        return toUserDTO(user);
    }

    // Hämta en användare via användarnamn
    public UserDTO getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User with username '" + username + "' not found."));
        return toUserDTO(user);
    }

    // Kontrollera om användarnamn eller e-post existerar
    public boolean isUsernameOrEmailTaken(String username, String email) {
        return userRepository.findByUsername(username).isPresent() || userRepository.findByEmail(email).isPresent();
    }

    // Skapa en ny användare
    public UserDTO createUser(User user) {
        if (isUsernameOrEmailTaken(user.getUsername(), user.getEmail())) {
            throw new IllegalArgumentException("Username or email already exists.");
        }
        User savedUser = userRepository.save(user);
        return toUserDTO(savedUser);
    }

    // Uppdatera en användares information
    public UserDTO updateUser(Long id, User updatedData) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User with ID " + id + " not found."));

        user.setUsername(updatedData.getUsername() != null ? updatedData.getUsername() : user.getUsername());
        user.setEmail(updatedData.getEmail() != null ? updatedData.getEmail() : user.getEmail());
        user.setPassword(updatedData.getPassword() != null ? updatedData.getPassword() : user.getPassword());

        User updatedUser = userRepository.save(user);
        return toUserDTO(updatedUser);
    }

    // Ta bort en användare
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User with ID " + id + " not found.");
        }
        userRepository.deleteById(id);
    }

    // Sök användare via nyckelord i användarnamn
    public List<UserDTO> searchUsersByKeyword(String keyword) {
        return userRepository.searchByUsernameContaining(keyword).stream()
                .map(this::toUserDTO)
                .collect(Collectors.toList());
    }

    // Hjälpmetod för att konvertera `User` till `UserDTO`
    private UserDTO toUserDTO(User user) {
        return new UserDTO(user.getId(), user.getUsername(), user.getEmail());
    }
}
