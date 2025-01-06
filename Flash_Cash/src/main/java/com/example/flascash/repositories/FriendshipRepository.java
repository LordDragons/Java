package com.example.flascash.repositories;

import com.example.flascash.entities.Friendship;
import com.example.flascash.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    Optional<Friendship> findByUserAndFriendUser(User user, User friendUser);

    @Query("SELECT f.friendUser FROM Friendship f WHERE f.user.id = :userId")
    List<User> findFriendsByUserId(@Param("userId") Long userId);

    @Query("SELECT u FROM User u " +
            "WHERE u.id IN (SELECT f.friendUser.id FROM Friendship f WHERE f.user.id = :userId " +
            "UNION " +
            "SELECT f.user.id FROM Friendship f WHERE f.friendUser.id = :userId)")
    List<User> findAllFriendsForUser(@Param("userId") Long userId);
}
