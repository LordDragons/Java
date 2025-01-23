package com.eservice.ms_guide.model;

import java.util.UUID;

public class Reward {
    private UUID rewardId;
    private String rewardName;
    private String description;

    // Constructors
    public Reward(UUID rewardId, String rewardName, String description) {
        this.rewardId = rewardId;
        this.rewardName = rewardName;
        this.description = description;
    }

    // Getters and Setters
    public UUID getRewardId() {
        return rewardId;
    }

    public void setRewardId(UUID rewardId) {
        this.rewardId = rewardId;
    }

    public String getRewardName() {
        return rewardName;
    }

    public void setRewardName(String rewardName) {
        this.rewardName = rewardName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
