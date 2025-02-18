package com.eservice.ms_rewards.service;

import com.eservice.ms_rewards.model.Rewards;
import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import rewardCentral.RewardCentral;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RewardsService {
    private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;

    private final int defaultProximityBuffer = 10;
    private int proximityBuffer = defaultProximityBuffer;
    private final GpsUtil gpsUtil;
    private final RewardCentral rewardsCentral;

    public RewardsService(GpsUtil gpsUtil, RewardCentral rewardCentral) {
        this.gpsUtil = gpsUtil;
        this.rewardsCentral = rewardCentral;
    }

    public void setProximityBuffer(int proximityBuffer) {
        this.proximityBuffer = proximityBuffer;
    }

    public void setDefaultProximityBuffer() {
        this.proximityBuffer = defaultProximityBuffer;
    }

//    public void calculateRewards(Long userId, List<VisitedLocation> userLocations, Set<Rewards> userRewards) {
//        Set<String> processedAttractions = new HashSet<>();
//        List<Attraction> attractions = gpsUtil.getAttractions();
//
//        for (VisitedLocation visitedLocation : userLocations) {
//            for (Attraction attraction : attractions) {
//                if (processedAttractions.add(attraction.attractionName)) {  // Avoid duplicate processing
//                    if (nearAttraction(visitedLocation, attraction)) {
//                        int rewardPoints = rewardsCentral.getAttractionRewardPoints(attraction.attractionId, userId);
//                        userRewards.add(new Rewards(userId, attraction, rewardPoints, visitedLocation));
//                    }
//                }
//            }
//        }
//    }

    public boolean isWithinAttractionProximity(Attraction attraction, Location location) {
        int attractionProximityRange = 200; // Fixed proximity range
        return getDistance(attraction, location) <= attractionProximityRange;
    }

    private boolean nearAttraction(VisitedLocation visitedLocation, Attraction attraction) {
        return getDistance(attraction, visitedLocation.location) <= proximityBuffer;
    }

    public double getDistance(Location loc1, Location loc2) {
        double lat1 = Math.toRadians(loc1.latitude);
        double lon1 = Math.toRadians(loc1.longitude);
        double lat2 = Math.toRadians(loc2.latitude);
        double lon2 = Math.toRadians(loc2.longitude);

        double angle = Math.acos(Math.sin(lat1) * Math.sin(lat2)
                + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));

        double nauticalMiles = 60 * Math.toDegrees(angle);
        return STATUTE_MILES_PER_NAUTICAL_MILE * nauticalMiles;
    }
}
