package com.example.flascash.service;

import com.example.flascash.entities.User;
import com.example.flascash.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(String email, String username, String password) {
        System.out.println("Registering user: email = " + email + ", username = " + username);

        if (userRepository.findByEmail(email).isPresent() || userRepository.findByUsername(username).isPresent()) {
            System.out.println("User already exists!");
            throw new IllegalArgumentException("Email or Username already exists");
        }

        String encodedPassword = passwordEncoder.encode(password);
        System.out.println("Encoded password: " + encodedPassword);

        User user = new User(email, encodedPassword, username);
        User savedUser = userRepository.save(user);
        System.out.println("User saved: " + savedUser.getId());
        return savedUser;
    }


    public List<User> findFriends(Long id) {
        return userRepository.findFriendsToAdd(id);
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public User findById(Long friendId) {
        return userRepository.findById(friendId).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }
}
