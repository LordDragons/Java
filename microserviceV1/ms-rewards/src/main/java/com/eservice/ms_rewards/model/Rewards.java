package com.eservice.ms_rewards.model;

import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;

public class Rewards {
    private Long userId;
    private Attraction attraction;
    private int rewardPoints;
    private VisitedLocation visitedLocation;

    public Rewards(Long userId, Attraction attraction, int rewardPoints, VisitedLocation visitedLocation) {
        this.userId = userId;
        this.attraction = attraction;
        this.rewardPoints = rewardPoints;
        this.visitedLocation = visitedLocation;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public VisitedLocation getVisitedLocation() {
        return visitedLocation;
    }

    public void setVisitedLocation(VisitedLocation visitedLocation) {
        this.visitedLocation = visitedLocation;
    }
}
