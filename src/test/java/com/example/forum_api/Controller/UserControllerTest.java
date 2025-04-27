package com.example.forum_api.Controller;

import com.example.forum_api.DTO.UserDTO;
import com.example.forum_api.models.User;
import com.example.forum_api.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.mockito.ArgumentMatchers.any;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class) //  Testtyp: Komponenttest av Controller
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService; // mockad tjänst

    @Autowired
    private ObjectMapper objectMapper;

    /**
     *  Komponenttest
     * Testar att GET /users/ returnerar 200 OK med en lista användare
     */
    @Test
    void getAllUsers_shouldReturnListOfUsers() throws Exception {
        List<UserDTO> users = List.of(
                new UserDTO(1L, "john", "john@example.com"),
                new UserDTO(2L, "jane", "jane@example.com")
        );

        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/users/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(2)))
                .andExpect(jsonPath("$[0].username", is("john")));
    }

    /**
     * ✅ Komponenttest
     * Testar att POST /users/ returnerar 201 CREATED med den skapade användaren
     */
    @Test
    void createUser_shouldReturnCreatedUser() throws Exception {
        User user = new User();
        user.setUsername("newuser");
        user.setEmail("newuser@example.com");
        user.setPassword("secret");

        UserDTO saved = new UserDTO(1L, user.getUsername(), user.getEmail());

        when(userService.createUser(any(User.class))).thenReturn(saved);

        mockMvc.perform(post("/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username", is("newuser")))
                .andExpect(jsonPath("$.email", is("newuser@example.com")));
    }
}
