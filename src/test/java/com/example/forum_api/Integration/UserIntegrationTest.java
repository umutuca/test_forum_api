package com.example.forum_api.Integration;

import com.example.forum_api.models.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 *  Integrationstest
 * Testar hela flödet från Controller → Service → Repository → MySQL
 * Testar skapande av användare via REST och säkerställer att den sparas i testdatabasen (forum_test).
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test") // Använd application-test.properties för testdatabas
public class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // Testa att skapa en användare
    @Test
    void createUser_shouldPersistToDatabase() throws Exception {
        User user = new User();
        user.setUsername("integration_test_user");
        user.setEmail("integration@example.com");
        user.setPassword("supersecret");

        mockMvc.perform(post("/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username", is("integration_test_user")))
                .andExpect(jsonPath("$.email", is("integration@example.com")));
    }
}
