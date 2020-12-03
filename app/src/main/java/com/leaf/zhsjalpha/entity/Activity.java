package com.leaf.zhsjalpha.entity;

public class Activity {
    private String activityId;
    private String imageUrl;
    private String activityName;
    private String activityDescription;
    private String activityAddress;
    private Integer maxCount;
    private String activityStartTime;
    private String activityEndTime;

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getActivityDescription() {
        return activityDescription;
    }

    public void setActivityDescription(String activityDescription) {
        this.activityDescription = activityDescription;
    }

    public String getActivityAddress() {
        return activityAddress;
    }

    public void setActivityAddress(String activityAddress) {
        this.activityAddress = activityAddress;
    }

    public Integer getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(Integer maxCount) {
        this.maxCount = maxCount;
    }

    public String getActivityStartTime() {
        return activityStartTime;
    }

    public void setActivityStartTime(String activityStartTime) {
        this.activityStartTime = activityStartTime;
    }

    public String getActivityEndTime() {
        return activityEndTime;
    }

    public void setActivityEndTime(String activityEndTime) {
        this.activityEndTime = activityEndTime;
    }

    @Override
    public String toString() {
        return "Activity{" +
                "activityId='" + activityId + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", activityName='" + activityName + '\'' +
                ", activityDescription='" + activityDescription + '\'' +
                ", activityAddress='" + activityAddress + '\'' +
                ", maxCount=" + maxCount +
                ", activityStartTime='" + activityStartTime + '\'' +
                ", activityEndTime='" + activityEndTime + '\'' +
                '}';
    }
}
