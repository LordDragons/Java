package com.example.flascash.service;

import com.example.flascash.entities.User;
import com.example.flascash.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.regex.Pattern;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Regex pour valider l'email (format basique)
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final String STRONG_PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";


    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(String email, String username, String password) {
        System.out.println("Registering user: email = " + email + ", username = " + username);

        // Valider l'email avec regex
        if (!Pattern.matches(EMAIL_REGEX, email)) {
            throw new IllegalArgumentException("Invalid email format.");
        }

        // Valider le mot de passe avec regex
        if (!Pattern.matches(STRONG_PASSWORD_REGEX, password)) {
            throw new IllegalArgumentException("Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character.");
        }

        // Vérifier si l'email ou le nom d'utilisateur existe déjà
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
