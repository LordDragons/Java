package com.eservice.ms_user.consumer;


import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class GuideGateway {
    private final RestTemplate restTemplate;

    public GuideGateway(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}