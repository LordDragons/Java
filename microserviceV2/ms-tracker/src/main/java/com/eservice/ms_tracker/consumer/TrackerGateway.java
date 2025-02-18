//package com.eservice.ms_tracker.consumer;
//
//import com.eservice.ms_user.model.User;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Component;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Objects;
//
//@Component
//public class TrackerGateway {
//
//    private final RestTemplate restTemplate;
//
//    // Injection via le constructeur sans la nécessité de @Autowired
//    public TrackerGateway(RestTemplate restTemplate) {
//        this.restTemplate = restTemplate;
//    }
//
//    // Méthode pour obtenir tous les utilisateurs
//    public List<User> getAllUsers() {
//        // Appel au microservice ms-user pour récupérer les utilisateurs
//        ResponseEntity<User[]> response = restTemplate.getForEntity(
//                "http://localhost:8080/user", User[].class);
//        // Retourne la liste des utilisateurs obtenus
//        return Arrays.asList(Objects.requireNonNull(response.getBody()));
//    }
//}
