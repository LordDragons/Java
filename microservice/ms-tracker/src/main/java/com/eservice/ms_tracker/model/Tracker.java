package com.eservice.ms_tracker.model;

public class Tracker {
    private Long userId;
    private String currentLocation;
    private Long lastUpdatedTime;

    public Tracker(Long userId, String currentLocation, Long lastUpdatedTime) {
        this.userId = userId;
        this.currentLocation = currentLocation;
        this.lastUpdatedTime = lastUpdatedTime;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public Long getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public void setLastUpdatedTime(Long lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
    }

    @Override
    public String toString() {
        return "Tracker{" +
                "userId=" + userId +
                ", currentLocation='" + currentLocation + '\'' +
                ", lastUpdatedTime=" + lastUpdatedTime +
                '}';
    }
}
