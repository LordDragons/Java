package com.eservice.ms_tracker.consumer;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class UserRewardsGateway {
    private final RestTemplate restTemplate;

    public UserRewardsGateway(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}