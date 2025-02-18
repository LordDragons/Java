package com.eservice.ms_user.model;

import gpsUtil.location.VisitedLocation;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class User {
    private final UUID userId;
    private String userName;
    private String phoneNumber;
    private String emailAddress;
    private Date latestLocationTimestamp;
    private final List<VisitedLocation> visitedLocations = new ArrayList<>();
    private final List<UserRewards> userRewards = new ArrayList<>();  // Liste des récompenses

    // Constructeur par défaut
    public User() {
        this.userId = UUID.randomUUID();
        this.userName = "DefaultUser";
        this.phoneNumber = "000-000-0000";
        this.emailAddress = "default@example.com";
    }

    // Constructeur avec tous les champs
    public User(UUID userId, String userName, String phoneNumber, String emailAddress, Date latestLocationTimestamp) {
        this.userId = userId;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
        this.latestLocationTimestamp = latestLocationTimestamp;
    }

    // Getters et Setters
    public UUID getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public Date getLatestLocationTimestamp() {
        return latestLocationTimestamp;
    }

    public void setLatestLocationTimestamp(Date latestLocationTimestamp) {
        this.latestLocationTimestamp = latestLocationTimestamp;
    }

    public void addToVisitedLocations(VisitedLocation visitedLocation) {
        visitedLocations.add(visitedLocation);
    }

    public List<VisitedLocation> getVisitedLocations() {
        return Collections.unmodifiableList(visitedLocations);
    }

    // Méthode pour ajouter une récompense à l'utilisateur
    public void addUserReward(UserRewards userReward) {
        if (userRewards.stream().noneMatch(r -> r.equals(userReward))) {
            userRewards.add(userReward);
        }
    }

    // Méthode pour obtenir les récompenses de l'utilisateur
    public List<UserRewards> getUserRewards() {
        return Collections.unmodifiableList(userRewards);
    }
}
