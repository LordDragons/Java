package com.eservice.ms_user.service;

import java.util.*;
import java.util.stream.Collectors;

import com.eservice.ms_user.consumer.PreferenceGateway;
import com.eservice.ms_user.model.User;
import gpsUtil.location.VisitedLocation;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tripPricer.Provider;


@Service
public class UserService {
    private final User user;
    private final List<VisitedLocation> visitedLocations = new ArrayList<>();
    private final List<UserRewardsService> userRewardsServices = new ArrayList<>();
    private List<Provider> tripDeals = new ArrayList<>();
    private PreferenceGateway preferenceGateway;

    public UserService(User user, PreferenceGateway preferenceGateway, RestTemplate restTemplate) {
        this.user = user;
        this.preferenceGateway = preferenceGateway;
    }

    public UserService() {
        this.user = new User();
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

    public List<String> getAllUsers() {
        if (preferenceGateway != null) {
            return preferenceGateway.getAllUsers().stream()
                    .map(UserService::getUserName)
                    .collect(Collectors.toList());
        }
        return Arrays.asList("User1", "User2", "User3");
    }

    public void addToVisitedLocations(VisitedLocation visitedLocation) {
        if (visitedLocation != null) {
            visitedLocations.add(visitedLocation);
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
