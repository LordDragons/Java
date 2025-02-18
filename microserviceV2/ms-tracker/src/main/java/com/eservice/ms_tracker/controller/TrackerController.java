//package com.eservice.ms_tracker.controller;
//
//import com.eservice.ms_tracker.service.TrackerService;
//import com.eservice.ms_user.model.User;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import java.util.List;
//
//@RestController
//@RequestMapping("/tracker")
//public class TrackerController {
//
//    private final TrackerService trackerService;
//
//    // Injection du service TrackerService
//    public TrackerController(TrackerService trackerService) {
//        this.trackerService = trackerService;
//    }
//
//    // Endpoint pour récupérer tous les utilisateurs depuis ms-user
//    @GetMapping("/all-users")
//    public List<User> getAllUsersFromMsUser() {
//        return trackerService.getUsersFromMsUser();  // Appel à la méthode du service pour récupérer les utilisateurs
//    }
//}
//
