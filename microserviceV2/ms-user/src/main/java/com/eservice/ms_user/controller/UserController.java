package com.eservice.ms_user.controller;


import com.eservice.ms_user.model.User;
import org.springframework.web.bind.annotation.*;
import com.eservice.ms_user.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Endpoint pour récupérer tous les utilisateurs via preferenceGateway
    @GetMapping("/all-preference")
    public List<User> getAllUsersFromPreferenceGateway() {
        return userService.getAllUsers();  // Appel à getAllUsers() pour récupérer les utilisateurs via preferenceGateway
    }

    // Endpoint pour récupérer tous les utilisateurs via trackerGateway
    @GetMapping("/all-tracker")
    public List<User> getAllUsersFromTrackerGateway() {
        return userService.getAllUsersFromTrackerGateway();  // Appel à getAllUsersFromTrackerGateway() pour récupérer via trackerGateway
    }

    @GetMapping("/test")
    public String testEndpoint() {
        return "Application is running!";
    }


}

