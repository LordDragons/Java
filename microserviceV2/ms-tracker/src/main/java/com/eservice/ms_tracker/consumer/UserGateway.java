//package com.eservice.ms_tracker.consumer;
//
//
//
//
//import com.eservice.ms_tracker.service.TrackerService;
//import com.eservice.ms_user.model.User;
//import com.eservice.ms_user.service.UserService;
//import gpsUtil.GpsUtil;
//import gpsUtil.location.VisitedLocation;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Component;
//import org.springframework.web.client.RestClientException;
//import org.springframework.web.client.RestTemplate;
//
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Objects;
//
//@Component
//public class UserGateway {
//    private final RestTemplate restTemplate;
//    private final GpsUtil gpsUtil;
//
//
//
//    public UserGateway(RestTemplate restTemplate, GpsUtil gpsUtil) {
//        this.restTemplate = restTemplate;
//        this.gpsUtil = gpsUtil;
//    }
//
//@Autowired
//    public UserGateway(RestTemplate restTemplate) {
//        this.restTemplate = restTemplate;
//        this.gpsUtil = null;
//    }
//
//
//    public void trackUserLocation(User user) {
//        try {
//            HttpEntity<Void> httpEntity = new HttpEntity<>(null);
//            ResponseEntity<UserService[]> response = restTemplate.exchange(
//                    "http://localhost:8080/user/",
//                    HttpMethod.GET,
//                    httpEntity,
//                    UserService[].class
//            );
//
//            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
//                UserService[] trackerServices = response.getBody();
//            }
//        } catch (RestClientException e) {
//            // Gérer l'exception proprement
//            System.err.println("Erreur lors de l'appel à l'API de suivi : " + e.getMessage());
//        }
//    }
//
//}
