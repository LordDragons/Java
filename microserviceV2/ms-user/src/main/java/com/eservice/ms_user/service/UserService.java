package com.eservice.ms_user.service;

import java.util.*;
import java.util.stream.Collectors;



import com.eservice.ms_user.consumer.PreferenceGateway;
import com.eservice.ms_user.consumer.TrackerGateway;
import com.eservice.ms_user.model.User;
import gpsUtil.location.VisitedLocation;
import org.springframework.stereotype.Service;
import tripPricer.Provider;


@Service
public class UserService {
    private final User user;
    private final List<VisitedLocation> visitedLocations = new ArrayList<>();
    private final List<UserRewardsService> userRewardsServices = new ArrayList<>();
    private List<Provider> tripDeals = new ArrayList<>();
    private PreferenceGateway preferenceGateway;
    private TrackerGateway trackerGateway;


    public UserService(User user, PreferenceGateway preferenceGateway, TrackerGateway trackerGateway) {
        this.user = new User();
        this.preferenceGateway = preferenceGateway;
        this.trackerGateway = trackerGateway;
    }

    public UUID getUserId() {
        return user.getUserId();
    }

    public String getUserName() {
        return user.getUserName();
    }

    public String getPhoneNumber() {
        return user.getPhoneNumber();
    }

    public String getEmailAddress() {
        return user.getEmailAddress();
    }

    public Date getLatestLocationTimestamp() {
        return user.getLatestLocationTimestamp();
    }

    public List<VisitedLocation> getVisitedLocations() {
        return Collections.unmodifiableList(visitedLocations);
    }

    public List<UserRewardsService> getUserRewards() {
        return Collections.unmodifiableList(userRewardsServices);
    }

    public List<Provider> getTripDeals() {
        return Collections.unmodifiableList(tripDeals);
    }

    public void setPhoneNumber(String phoneNumber) {
        user.setPhoneNumber(phoneNumber);
    }

    public void setEmailAddress(String emailAddress) {
        user.setEmailAddress(emailAddress);
    }

    public void setLatestLocationTimestamp(Date latestLocationTimestamp) {
        user.setLatestLocationTimestamp(latestLocationTimestamp);
    }

    public List<User> getAllUsers() {
        if (preferenceGateway != null) {
            return preferenceGateway.getAllUsers();
        }

        // Créer des utilisateurs fictifs avec le constructeur par défaut
        User user1 = new User();
        user1.setUserName("User1");
        user1.setEmailAddress("user1@example.com");

        User user2 = new User();
        user2.setUserName("User2");
        user2.setEmailAddress("user2@example.com");

        return Arrays.asList(user1, user2);
    }
    public List<User> getAllUsersFromTrackerGateway() {
        if (trackerGateway != null) {
            return trackerGateway.getAllUsers();
        }

        // Retourner des utilisateurs fictifs si trackerGateway est null
        User user1 = new User();
        user1.setUserName("User3");
        user1.setEmailAddress("user3@example.com");

        User user2 = new User();
        user2.setUserName("User4");
        user2.setEmailAddress("user4@example.com");

        return Arrays.asList(user1, user2);
    }

    public void addToVisitedLocations(User user, VisitedLocation visitedLocation) {
        if (user != null && visitedLocation != null) {
            user.addToVisitedLocations(visitedLocation);
            System.out.println("Visited location added: " + visitedLocation);
        }
    }

    public void clearVisitedLocations() {
        visitedLocations.clear();
    }

    public VisitedLocation getLastVisitedLocation() {
        if (!visitedLocations.isEmpty()) {
            return visitedLocations.getLast();
        }
        return null;
    }

    public void addUserReward(UserRewardsService userReward) {
        if (userReward != null && userRewardsServices.stream().noneMatch(r ->
                r.getAttraction().equals(userReward.getAttraction()) &&
                        r.getVisitedLocation().equals(userReward.getVisitedLocation()))) {
            userRewardsServices.add(userReward);
        }
    }

    public void setTripDeals(List<Provider> tripDeals) {
        if (tripDeals != null) {
            this.tripDeals = tripDeals;
        }
    }

    public PreferenceGateway getPreferenceGateway() {
        return preferenceGateway;
    }

    public void setPreferenceGateway(PreferenceGateway preferenceGateway) {
        this.preferenceGateway = preferenceGateway;
    }

}
