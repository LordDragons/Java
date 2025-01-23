//package com.eservice.ms_guide.consumer;
//
//import com.eservice.ms_user.model.User;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Component;
//import org.springframework.web.client.RestTemplate;
//
//@Component
//public class UserGateway {
//    private final RestTemplate restTemplate;
//
//    public UserGateway(RestTemplate restTemplate) {
//        this.restTemplate = restTemplate;
//    }
//
//    public User getUserId() {
//            HttpEntity<Void> httpEntity = new HttpEntity<>(null);
//            ResponseEntity<User> response = restTemplate.getForEntity(
//                    "http://localhost:8080/user", User.class, httpEntity);
//            return response.getBody();
//    }
//}
