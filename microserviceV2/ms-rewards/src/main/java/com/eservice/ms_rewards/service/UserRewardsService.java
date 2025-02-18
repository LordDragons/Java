package com.eservice.ms_rewards.service;

import com.eservice.ms_rewards.model.UserRewards;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserRewardsService {
    private final List<UserRewards> userRewardsList;

    public UserRewardsService() {
        this.userRewardsList = new ArrayList<>();
    }

    /**
     * Ajoute une récompense pour un utilisateur.
     *
     * @param visitedLocation La localisation visitée.
     * @param attraction      L'attraction liée à la récompense.
     * @param rewardPoints    Les points de récompense associés.
     */
    public void addReward(VisitedLocation visitedLocation, Attraction attraction, int rewardPoints) {
        UserRewards userRewards = new UserRewards(visitedLocation, attraction, rewardPoints);
        userRewardsList.add(userRewards);
    }

    /**
     * Récupère la liste des récompenses pour l'utilisateur.
     *
     * @return Liste de récompenses.
     */
    public List<UserRewards> getUserRewards() {
        return new ArrayList<>(userRewardsList); // Renvoie une copie pour éviter les modifications externes.
    }

    /**
     * Vérifie si une attraction a déjà été récompensée pour un utilisateur.
     *
     * @param attraction L'attraction à vérifier.
     * @return `true` si la récompense existe, sinon `false`.
     */
    public boolean hasRewardForAttraction(Attraction attraction) {
        return userRewardsList.stream()
                .anyMatch(reward -> reward.getAttraction().equals(attraction));
    }

    @Override
    public String toString() {
        return "UserRewardsService{" +
                "userRewardsList=" + userRewardsList +
                '}';
    }
}
