package com.eservice.ms_user.consumer;

import com.eservice.ms_user.model.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
public class PreferenceGateway {
    private final RestTemplate restTemplate;

    public PreferenceGateway(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<User> getAllUsers() {
        try {
            HttpEntity<Void> httpEntity = new HttpEntity<>(null);
            ResponseEntity<User[]> response = restTemplate.getForEntity(
                    "http://localhost:8081/preferences/", User[].class, httpEntity);
            if (response.getStatusCode().is2xxSuccessful()) {
                // Correct cast
                return Arrays.asList(Objects.requireNonNull(response.getBody()));
            } else {
                throw new RuntimeException("Failed to fetch user preferences");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("An error occurred while fetching user preferences", e);
        }
    }
}
