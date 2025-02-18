package com.eservice.ms_tracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class TrackerConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
