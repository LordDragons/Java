package com.example.flascash.repositories;

import com.example.flascash.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);

    @Query("SELECT u FROM User u " +
            "WHERE u.id != :currentUserId " +
            "AND u NOT IN (" +
            "SELECT f.friendUser FROM Friendship f WHERE f.user.id = :currentUserId" +
            "   UNION " +
            "   SELECT f.user FROM Friendship f WHERE f.friendUser.id = :currentUserId" +
            ")")
    List<User> findFriendsToAdd(@Param("currentUserId") Long currentUserId);

}
