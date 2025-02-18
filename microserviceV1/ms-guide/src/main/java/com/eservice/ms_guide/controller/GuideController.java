package com.eservice.ms_guide.controller;

import com.eservice.ms_guide.model.Guide;
import gpsUtil.GpsUtil;
import com.eservice.ms_guide.service.GuideService;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api")
public class GuideController {
    private final GuideService guideService;
    private final GpsUtil gpsUtil;

    public GuideController(GuideService guideService) {
        this.guideService = guideService;
        this.gpsUtil = new GpsUtil();
    }

    @GetMapping("/users")
    public List<Guide> getAllUsers() {
        return guideService.getAllUsers();
    }

    @GetMapping("/allAttractions")
    public List<Attraction> getAllAttractions() {
        return gpsUtil.getAttractions();
    }

    @GetMapping("/attraction")
    public Attraction getAttractionByName(@RequestParam String name) {
        return gpsUtil.getAttractions().stream()
                .filter(attraction -> attraction.attractionName.equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Attraction not found"));
    }



    @PostMapping("/{userName}/trackLocation")
    public VisitedLocation trackUserLocation(@PathVariable String userName) {
        Guide user = guideService.getUser(userName);
        return guideService.trackUserLocation(user);
    }
}
