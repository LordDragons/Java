package com.eservice.ms_user.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.eservice.ms_user.service.UserService;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<String>> getAllUsers() {
        List<String> users = userService.getAllUsers();
        return ResponseEntity.ok(users != null ? users : Collections.emptyList());
    }

    @GetMapping("/test")
    public String testEndpoint() {
        return "Application is running!";
    }
}

