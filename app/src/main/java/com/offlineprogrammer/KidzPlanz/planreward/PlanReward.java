package com.offlineprogrammer.KidzPlanz.planreward;

public class PlanReward {

    private String planRewardName;
    private String planRewardImage;


    public PlanReward(String planRewardName, String planRewardImage) {
        this.planRewardName = planRewardName;
        this.planRewardImage = planRewardImage;
    }

    public String getPlanRewardImage() {
        return planRewardImage;
    }

    public void setPlanRewardImage(String planRewardImage) {
        this.planRewardImage = planRewardImage;
    }

    public String getPlanRewardName() {
        return planRewardName;
    }

    public void setPlanRewardName(String planRewardName) {
        this.planRewardName = planRewardName;
    }
}
