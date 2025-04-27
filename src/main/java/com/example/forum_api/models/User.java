package com.example.forum_api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "username"))
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email; // Lägger till email för identifiering

    @Column(nullable = false)
    private String password; // Skapa fält för lösenord (bör hashas vid användning)

    @Column(updatable = false)
    private LocalDateTime createdAt;

    // Automatiskt sätta createdAt vid skapelse
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // Getter för id
    public Long getId() {
        return id;
    }

    // Setter för id
    public void setId(Long id) {
        this.id = id;
    }

    // Getter för username
    public String getUsername() {
        return this.username;
    }

    // Setter för username
    public void setUsername(String username) {
        this.username = username;
    }

    // Getter för email
    public String getEmail() {
        return this.email;
    }

    // Setter för email
    public void setEmail(String email) {
        this.email = email;
    }

    // Getter för password
    public String getPassword() {
        return this.password;
    }

    // Setter för password
    public void setPassword(String password) {
        this.password = password;
    }

    // Getter för createdAt
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // Setter för createdAt
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Tydlig toString-metod för loggning
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
