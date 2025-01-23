//package com.eservice.ms_tracker.consumer;
//
//import com.eservice.ms_user.service.*;
//import gpsUtil.location.VisitedLocation;
//import org.springframework.stereotype.Component;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.Arrays;
//import java.util.List;
//
//@Component
//public class GuideGateway {
//    private final RestTemplate restTemplate;
//
//    public GuideGateway(RestTemplate restTemplate) {
//        this.restTemplate = restTemplate;
//    }
//
//    public List<UserService> getAllUsers() {
//        String url = "http://ms-guide/api/users";
//        UserService[] users = restTemplate.getForObject(url, UserService[].class);
//        assert users != null;
//        return Arrays.asList(users);
//    }
//
//    public void trackUserLocation(UserService user) {
//        String url = "http://ms-guide/api/users/" + user.getUserName() + "/trackLocation";
//        restTemplate.postForObject(url, null, VisitedLocation.class);
//    }
//}
