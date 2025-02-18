package com.eservice.ms_rewards.controller;

import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;

public class RewardsRequest {
    private VisitedLocation visitedLocation;
    private Attraction attraction;
    private int rewardPoints;

    // Getters et Setters
    public VisitedLocation getVisitedLocation() {
        return visitedLocation;
    }

    public void setVisitedLocation(VisitedLocation visitedLocation) {
        this.visitedLocation = visitedLocation;
    }

    public Attraction getAttraction() {
        return attraction;
    }

    public void setAttraction(Attraction attraction) {
        this.attraction = attraction;
    }

    public int getRewardPoints() {
        return rewardPoints;
    }

    public void setRewardPoints(int rewardPoints) {
        this.rewardPoints = rewardPoints;
    }
}
