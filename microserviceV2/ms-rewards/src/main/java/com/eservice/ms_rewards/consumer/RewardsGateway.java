//package com.eservice.ms_rewards.consumer;
//
//
//import com.eservice.ms_user.service.UserService;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Component;
//import org.springframework.web.client.RestTemplate;
//
//
//import java.util.UUID;
//
//@Component
//public class RewardsGateway {
//    private final RestTemplate restTemplate;
//
//    public RewardsGateway(RestTemplate restTemplate) {
//        this.restTemplate = restTemplate;
//    }
//
//    public UserService getUser(UUID userId) {
//        HttpEntity<Void> httpEntity = new HttpEntity<>(null);
//        ResponseEntity<UserService> response = restTemplate.getForEntity(
//               "http://localhost:8080/user/" + userId, UserService.class, httpEntity);
//       return response.getBody();
//    }
//}
//
