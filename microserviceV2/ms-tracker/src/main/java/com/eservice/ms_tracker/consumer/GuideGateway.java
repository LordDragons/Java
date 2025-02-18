package com.eservice.ms_tracker.consumer;


import gpsUtil.location.VisitedLocation;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
public class GuideGateway {
    private final RestTemplate restTemplate;

    public GuideGateway(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void trackUserLocation() {
        // Récupération des localisations visitées
        List<VisitedLocation> visitedLocations = getVisitedLocations();

        // Traitement des localisations
        for (VisitedLocation location : visitedLocations) {
            System.out.println("User visited location: " + location);
        }
    }

    private List<VisitedLocation> getVisitedLocations() {
        String url = "http://localhost:8080/api/visitedLocations"; // Remplacez par l'URL correcte
        VisitedLocation[] locations = restTemplate.getForObject(url, VisitedLocation[].class);
        return locations != null ? Arrays.asList(locations) : List.of();
    }
}
