package com.example.flascash.service;

import com.example.flascash.entities.User;
import com.example.flascash.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SessionService {

    private final UserRepository userRepository;

    public SessionService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User sessionUser() {
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return userRepository.findByEmail(user.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
