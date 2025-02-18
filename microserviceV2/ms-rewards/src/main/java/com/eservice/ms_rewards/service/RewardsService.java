//package com.eservice.ms_rewards.service;
//
//
//import com.eservice.ms_user.model.User;
//import gpsUtil.GpsUtil;
//import gpsUtil.location.Attraction;
//import gpsUtil.location.Location;
//import gpsUtil.location.VisitedLocation;
//import org.springframework.stereotype.Service;
//import rewardCentral.RewardCentral;
//
//
//@Service
//public class RewardsService {
//    private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;
//
//    private final int defaultProximityBuffer = 10;
//    private int proximityBuffer = defaultProximityBuffer;
//    private final GpsUtil gpsUtil;
//    private final RewardCentral rewardCentral;
//
//    public RewardsService(GpsUtil gpsUtil, RewardCentral rewardCentral) {
//        this.gpsUtil = gpsUtil;
//        this.rewardCentral = rewardCentral;
//    }
//
//
//    public void setProximityBuffer(int proximityBuffer) {
//        this.proximityBuffer = proximityBuffer;
//    }
//
//    public void setDefaultProximityBuffer() {
//        this.proximityBuffer = defaultProximityBuffer;
//    }
//
//
//    public boolean isWithinAttractionProximity(Attraction attraction, Location location) {
//        int attractionProximityRange = 200; // Fixed proximity range
//        return getDistance(attraction, location) <= attractionProximityRange;
//    }
//
//    private boolean nearAttraction(VisitedLocation visitedLocation, Attraction attraction) {
//        return getDistance(attraction, visitedLocation.location) <= proximityBuffer;
//    }
//
//    public double getDistance(Location loc1, Location loc2) {
//        double lat1 = Math.toRadians(loc1.latitude);
//        double lon1 = Math.toRadians(loc1.longitude);
//        double lat2 = Math.toRadians(loc2.latitude);
//        double lon2 = Math.toRadians(loc2.longitude);
//
//        double angle = Math.acos(Math.sin(lat1) * Math.sin(lat2)
//                + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));
//
//        double nauticalMiles = 60 * Math.toDegrees(angle);
//        return STATUTE_MILES_PER_NAUTICAL_MILE * nauticalMiles;
//    }
//
//    public void calculateRewards(User user) {
//        return;
//    }
//
////    public void calculateRewards(User user) {
////        return;
////    }
//}
