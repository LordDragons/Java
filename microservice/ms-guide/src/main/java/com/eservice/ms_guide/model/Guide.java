package com.eservice.ms_guide.model;

import gpsUtil.location.VisitedLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Guide {
    private UUID userId;
    private String userName;
    private String phone;
    private String email;
    private final List<VisitedLocation> visitedLocations = new ArrayList<>();
    private final List<Reward> userRewards = new ArrayList<>();

    // Constructors, getters, and setters

    public Guide(UUID userId, String userName, String phone, String email) {
        this.userId = userId;
        this.userName = userName;
        this.phone = phone;
        this.email = email;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<VisitedLocation> getVisitedLocations() {
        return visitedLocations;
    }

    public void addVisitedLocation(VisitedLocation visitedLocation) {
        this.visitedLocations.add(visitedLocation);
    }

    public List<Reward> getUserRewards() {
        return userRewards;
    }

    public void addUserReward(Reward reward) {
        this.userRewards.add(reward);
    }
}
