package com.example.forum_api.Repository;

import com.example.forum_api.models.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ChannelRepository extends JpaRepository<Channel, Long> {

    // Hitta en kanal baserat på dess namn
    Optional<Channel> findByName(String name);

    // Hitta alla kanaler skapade av en viss användare (baserat på användarens id)
    List<Channel> findByCreatedById(Long userId);

    // Hitta alla kanaler skapade efter ett visst datum
    List<Channel> findByCreatedAtAfter(LocalDateTime dateTime);

    // Hitta alla kanaler som innehåller ett visst ord i namnet (case-insensitive)
    @Query("SELECT c FROM Channel c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Channel> searchByNameContaining(@Param("keyword") String keyword);

    // Hämta kanaler baserat på användarnamn på skaparen (Custom Query)
    @Query("SELECT c FROM Channel c WHERE c.createdBy.username = :username")
    List<Channel> findByCreatorUsername(@Param("username") String username);

    // Hitta alla kanaler som har ett specifikt namn och skapades före ett visst datum
    @Query("SELECT c FROM Channel c WHERE c.name = :name AND c.createdAt < :date")
    List<Channel> findByNameAndCreatedBefore(@Param("name") String name, @Param("date") LocalDateTime date);
}
