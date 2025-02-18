package com.eservice.ms_rewards.controller;

import com.eservice.ms_rewards.model.UserRewards;
import com.eservice.ms_rewards.service.UserRewardsService;
import gpsUtil.location.Attraction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rewards")
public class UserRewardsController {

    @Autowired
    private UserRewardsService userRewardsService;


    @PostMapping("/add")
    public void addReward(@RequestBody RewardsRequest rewardsRequest) {
        userRewardsService.addReward(
                rewardsRequest.getVisitedLocation(),
                rewardsRequest.getAttraction(),
                rewardsRequest.getRewardPoints()
        );
    }

    /**
     * Récupère toutes les récompenses d’un utilisateur.
     *
     * @return Liste des récompenses utilisateur.
     */
    @GetMapping
    public List<UserRewards> getUserRewards() {
        return userRewardsService.getUserRewards();
    }

    /**
     * Vérifie si une attraction a déjà été récompensée.
     *
     * @param attraction L'attraction à vérifier.
     * @return `true` si une récompense existe, sinon `false`.
     */
    @GetMapping("/exists")
    public boolean hasRewardForAttraction(@RequestParam Attraction attraction) {
        return userRewardsService.hasRewardForAttraction(attraction);
    }
}
