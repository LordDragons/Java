package com.example.flascash.controller;

import com.example.flascash.entities.User;
import com.example.flascash.repositories.UserRepository;
import com.example.flascash.service.FriendshipService;
import com.example.flascash.service.SessionService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class HomeController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SessionService sessionService;
    private final FriendshipService friendshipService;

    public HomeController(UserRepository userRepository, PasswordEncoder passwordEncoder, SessionService sessionService, FriendshipService friendshipService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.sessionService = sessionService;
        this.friendshipService = friendshipService;
    }

    @RequestMapping(path = "/")
    public String home() {
        return "home";
    }

    @GetMapping(path = "/dashboard")
    public String dashboard(Model model) {
        User user = sessionService.sessionUser();
        List<User> friends = friendshipService.getAllFriends(user.getId());
        model.addAttribute("friends", friends);
        return "dashboard";
    }
}
