package com.eservice.ms_guide.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class GuideConfig {
    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}