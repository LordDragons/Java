package com.eservice.ms_user.model;

import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import gpsUtil.GpsUtil;
import rewardCentral.RewardCentral;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserRewards {
    private final VisitedLocation visitedLocation;
    private final Attraction attraction;
    private final int rewardPoints;
    private final RewardCentral rewardsCentral;
    private final GpsUtil gpsUtil;

    public UserRewards(VisitedLocation visitedLocation, Attraction attraction, int rewardPoints) {
        this.visitedLocation = visitedLocation;
        this.attraction = attraction;
        this.rewardPoints = rewardPoints;
        this.rewardsCentral = null;
        this.gpsUtil = null;
    }

    public VisitedLocation getVisitedLocation() {
        return visitedLocation;
    }

    public Attraction getAttraction() {
        return attraction;
    }

    public int getRewardPoints() {
        return rewardPoints;
    }

    @Override
    public String toString() {
        return "UserRewards{" +
                "visitedLocation=" + visitedLocation +
                ", attraction=" + attraction +
                ", rewardPoints=" + rewardPoints +
                '}';
    }

    private int getRewardPoints(Attraction attraction, User user) {
        assert rewardsCentral != null;
        return rewardsCentral.getAttractionRewardPoints(attraction.attractionId, user.getUserId());
    }

    public void calculateRewards(User user) {
        if (user == null || gpsUtil == null) return;

        List<VisitedLocation> userLocations = user.getVisitedLocations();
        if (userLocations == null || userLocations.isEmpty()) return;

        List<Attraction> attractions = gpsUtil.getAttractions();

        for (VisitedLocation visitedLocation : userLocations) {
            for (Attraction attraction : attractions) {
                if (nearAttraction(visitedLocation, attraction)) {
                    int rewardPoints = getRewardPoints(attraction, user);
                    user.addUserReward(new UserRewards(visitedLocation, attraction, rewardPoints));  // Assuming 'addUserReward' method in 'User'
                }
            }
        }
    }

    private boolean nearAttraction(VisitedLocation visitedLocation, Attraction attraction) {
        final double proximityBuffer = 10.0;
        return getDistance(visitedLocation, attraction) < proximityBuffer;
    }

    private double getDistance(VisitedLocation visitedLocation, Attraction attraction) {
        return Math.sqrt(Math.pow(visitedLocation.location.latitude - attraction.latitude, 2) +
                Math.pow(visitedLocation.location.longitude - attraction.longitude, 2));
    }
}
