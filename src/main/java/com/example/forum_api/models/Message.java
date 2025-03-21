package com.example.forum_api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY) // Förbättrad prestanda med Lazy-loading
    @JoinColumn(name = "channel_id", nullable = false)
    private Channel channel;

    @ManyToOne(fetch = FetchType.LAZY) // Förbättrad prestanda med Lazy-loading
    @JoinColumn(name = "user_id", nullable = false)
    private User createdBy;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    // Extra konstruktor för enkel instansiering
    public Message(String content, Channel channel, User createdBy) {
        this.content = content;
        this.channel = channel;
        this.createdBy = createdBy;
        this.timestamp = LocalDateTime.now();
    }

    // Standardkonstruktor (nödvändig för Hibernate och JPA)
    public Message() {
        // Hibernate kräver denna konstruktor
    }

    // Initierar timestamp vid persistens om det inte redan är satt
    @PrePersist
    protected void onCreate() {
        if (this.timestamp == null) {
            this.timestamp = LocalDateTime.now();
        }
    }

    // Manuellt definierade getters och setters
    public Long getId() {
        return id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    // Förbättrad `toString`-metod för tydlig loggning och debugging
    @Override
    public String toString() {
        return "Message{" +
                "Message-id=" + id +
                ", content='" + content + '\'' +
                ", timestamp=" + timestamp +
                ", channel=" + (channel != null ? channel.getName() : "null") +
                ", createdBy=" + (createdBy != null ? createdBy.getUsername() : "null") +
                '}';
    }
}
