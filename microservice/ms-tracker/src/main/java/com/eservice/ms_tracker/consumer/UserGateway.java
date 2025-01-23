package com.eservice.ms_tracker.consumer;


import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.eservice.ms_user.service.UserService;

@Component
public class UserGateway {
    private final RestTemplate restTemplate;

    public UserGateway(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public UserService getAllUsers() {
        HttpEntity<Void> httpEntity = new HttpEntity<>(null);
        ResponseEntity<UserService> response = restTemplate.getForEntity(
                "http://localhost:8080/user", UserService.class, httpEntity);
        return response.getBody();
    }
}
