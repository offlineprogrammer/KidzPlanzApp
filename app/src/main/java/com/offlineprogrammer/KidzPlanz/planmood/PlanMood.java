package com.offlineprogrammer.KidzPlanz.planmood;

public class PlanMood {

    private String planMoodName;
    private String planMoodImage;


    public PlanMood(String planMoodName, String planMoodImage) {
        this.planMoodName = planMoodName;
        this.planMoodImage = planMoodImage;
    }

    public String getPlanMoodImage() {
        return planMoodImage;
    }

    public void setPlanMoodImage(String planMoodImage) {
        this.planMoodImage = planMoodImage;
    }

    public String getPlanMoodName() {
        return planMoodName;
    }

    public void setPlanMoodName(String planMoodName) {
        this.planMoodName = planMoodName;
    }
}
