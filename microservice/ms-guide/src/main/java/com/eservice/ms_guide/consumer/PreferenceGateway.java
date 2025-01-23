//package com.eservice.ms_guide.consumer;
//
//import org.springframework.http.HttpEntity;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Component;
//import org.springframework.web.client.RestTemplate;
//import com.eservice.ms_user.service.UserService;
//
//import java.util.*;
//
//@Component
//public class PreferenceGateway {
//    private final RestTemplate restTemplate;
//
//    public PreferenceGateway(RestTemplate restTemplate) {
//        this.restTemplate = restTemplate;
//    }
//
//    public List<UserService> getAllUsers() {
//        HttpEntity<Void> httpEntity = new HttpEntity<>(null);
//        ResponseEntity<UserService[]> response = restTemplate.getForEntity("http://localhost:8081/preferences/", PreferenceService[].class,httpEntity);
//        return Arrays.asList(Objects.requireNonNull(response.getBody()));
//    }
//
//}