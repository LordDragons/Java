package com.eservice.ms_guide.service;


import com.eservice.ms_guide.model.Guide;
import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tripPricer.TripPricer;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GuideService {
    private final Logger logger = LoggerFactory.getLogger(GuideService.class);
    private final GpsUtil gpsUtil = new GpsUtil();
    private final TripPricer tripPricer = new TripPricer();
    private final Map<String, Guide> userMap = new HashMap<>();


    public Guide getUser() {
        String userName = "";
        return userMap.get(userName);
    }

    public List<Guide> getAllUsers() {
        return new ArrayList<>(userMap.values());
    }

    public void addUser(Guide user) {
        userMap.put(user.getUserName(), user);
    }

    public VisitedLocation trackUserLocation(Guide user) {
        VisitedLocation visitedLocation = gpsUtil.getUserLocation(user.getUserId());
        user.addVisitedLocation(visitedLocation);
        return visitedLocation;
    }

    public List<Attraction> getNearbyAttractions(VisitedLocation visitedLocation) {
        return gpsUtil.getAttractions().stream()
                .filter(attraction -> isWithinAttractionProximity(attraction, visitedLocation.location))
                .collect(Collectors.toList());
    }

    private boolean isWithinAttractionProximity(Attraction attraction, Location location) {
        // Proximity logic
        return true; // Placeholder, replace with actual calculation
    }

}
