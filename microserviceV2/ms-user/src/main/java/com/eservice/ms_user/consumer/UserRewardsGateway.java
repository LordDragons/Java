package com.eservice.ms_user.consumer;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class UserRewardsGateway {
    @Autowired
    private final RestTemplate restTemplate;

    public UserRewardsGateway(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
