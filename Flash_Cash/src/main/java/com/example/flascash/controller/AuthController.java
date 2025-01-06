package com.example.flascash.controller;

import com.example.flascash.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {
    @Autowired
    private UserService userService;

    @GetMapping("/signup")
    public String signup() {
        System.out.println("Rendering signup page...");
        return "signup";
    }

    @PostMapping("/register")
    public String register(@RequestParam String email,
                           @RequestParam String username,
                           @RequestParam String password,
                           Model model) {
        System.out.println("Incoming registration: email = " + email + ", username = " + username);

        try {
            userService.registerUser(email, username, password);
            System.out.println("User registered successfully!");
            return "redirect:/signin";
        } catch (IllegalArgumentException e) {
            System.out.println("Error during registration: " + e.getMessage());
            model.addAttribute("error", e.getMessage());
            return "signup";
        }
    }

    @GetMapping("/signin")
    public String signin() {
        return "signin";
    }
}
