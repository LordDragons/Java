package com.eservice.ms_preference.consumer;


import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


import com.eservice.ms_preference.service.PreferenceService;

import java.util.*;

@Component
public class PreferenceGateway {
    private final RestTemplate restTemplate;

    public PreferenceGateway(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public PreferenceService getPreferences(UUID userId) {
        HttpEntity<Void> httpEntity = new HttpEntity<>(null);
        ResponseEntity<PreferenceService> response = restTemplate.getForEntity(
                "http://localhost:8081/preferences/" + userId, PreferenceService.class, httpEntity);
        return response.getBody();
    }
}