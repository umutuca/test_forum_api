package com.example.forum_api.Repository;

import com.example.forum_api.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    // Hitta alla meddelanden för en viss kanal
    List<Message> findByChannelId(Long channelId);

    // Hitta alla meddelanden skapade efter en viss tidpunkt
    List<Message> findByTimestampAfter(LocalDateTime timestamp);

    // Hitta alla meddelanden som innehåller ett visst nyckelord i innehållet
    @Query("SELECT m FROM Message m WHERE LOWER(m.content) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Message> searchByContentContaining(@Param("keyword") String keyword);

    // Hitta alla meddelanden skapade av en viss användare baserat på användarens ID
    List<Message> findByCreatedById(Long userId);

    // Hitta alla meddelanden för en viss kanal som skapades före ett visst datum
    @Query("SELECT m FROM Message m WHERE m.channel.id = :channelId AND m.timestamp < :timestamp")
    List<Message> findByChannelIdAndCreatedBefore(@Param("channelId") Long channelId, @Param("timestamp") LocalDateTime timestamp);

}
