package com.example.forum_api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "channels")
public class Channel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id") // Inkluderar ID i JSON-respons
    private Long id;

    @Column(nullable = false, unique = true)
    @JsonProperty("name") // Inkluderar kanalnamn i JSON-respons
    private String name;

    @Column(nullable = true)
    @JsonProperty("description") // Beskrivning av kanalen
    private String description;

    @ManyToOne(fetch = FetchType.LAZY) // Lazy-loading för bättre prestanda
    @JoinColumn(name = "user_id", nullable = false) // Matchar din SQL-kolumn user_id
    @JsonProperty("createdBy") // Inkluderar användardata i JSON-respons
    @JsonIgnore
    private User createdBy;

    @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore // Undvik cirkulära referenser i JSON
    private List<Message> messages;

    @Column(name = "created_at", nullable = false, updatable = false)
    @JsonProperty("createdAt") // Inkluderar tidpunkt i JSON-respons
    private LocalDateTime createdAt;

    public Channel() {}

    public Channel(String name, String description, User createdBy) {
        this.name = name;
        this.description = description;
        this.createdBy = createdBy;
        this.createdAt = LocalDateTime.now();
    }

    // Getter och Setter för 'id'
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // Getter och Setter för 'name'
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter och Setter för 'description'
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Getter och Setter för 'createdBy'
    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    // Getter och Setter för 'messages'
    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    // Getter och Setter för 'createdAt'
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Överlagrad toString-metod för debugging och loggning
    @Override
    public String toString() {
        return "Channel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + (description != null ? description : "null") + '\'' +
                ", createdBy=" + (createdBy != null ? createdBy.getUsername() : "null") +
                ", createdAt=" + createdAt +
                '}';
    }

    // Initierar createdAt när objektet sparas i databasen första gången
    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }
}
