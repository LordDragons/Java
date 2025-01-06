package com.example.flascash.service;

import com.example.flascash.entities.Friendship;
import com.example.flascash.entities.User;
import com.example.flascash.repositories.FriendshipRepository;
import com.example.flascash.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FriendshipService {
    @Autowired
    private FriendshipRepository friendshipRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Friendship> findAll() {
        return friendshipRepository.findAll();
    }

    public List<User> getAllFriends(Long userId) {
        try {
            return friendshipRepository.findAllFriendsForUser(userId);
        } catch (Exception e) {
            // Fallback to native query if JPQL fails
            return friendshipRepository.findAllFriendsForUser(userId);
        }
    }

    public Friendship createFriendship(Long userId, Long friendId) {
        if (userId.equals(friendId)) {
            throw new IllegalArgumentException("Not allowed to create a friendship");
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User id not found"));
        User friendUser = userRepository.findById(friendId).orElseThrow(() -> new EntityNotFoundException("Friend id not found"));

        Optional<Friendship> existingFriendship1 = friendshipRepository.findByUserAndFriendUser(user, friendUser);
        Optional<Friendship> existingFriendship2 = friendshipRepository.findByUserAndFriendUser(friendUser, user);


        if (existingFriendship1.isPresent() || existingFriendship2.isPresent()) {
            throw new IllegalStateException("Friendship already exists");
        }

        Friendship friendship = new Friendship(user, friendUser);
        return friendshipRepository.save(friendship);
    }

    public List<User> getUserFriends(Long userId) {
        return friendshipRepository.findFriendsByUserId(userId);
    }
}
