package com.example.forum_api.Repository;

import com.example.forum_api.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Hitta en användare baserat på användarnamn
    Optional<User> findByUsername(String username);

    // Kontrollera om en e-postadress redan existerar
    Optional<User> findByEmail(String email);

    // Hitta alla användare som har ett visst ord i sitt användarnamn
    @Query("SELECT u FROM User u WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<User> searchByUsernameContaining(@Param("keyword") String keyword);

    // Hitta alla användare som skapades före ett visst datum
    @Query("SELECT u FROM User u WHERE u.createdAt < :date")
    List<User> findByCreatedBefore(@Param("date") LocalDateTime date);

    // Kontrollera om användarnamn eller e-postadress existerar (kombinerad kontroll)
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN TRUE ELSE FALSE END FROM User u WHERE u.username = :username OR u.email = :email")
    boolean existsByUsernameOrEmail(@Param("username") String username, @Param("email") String email);
}
