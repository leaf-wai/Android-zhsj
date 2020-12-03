package com.leaf.zhsjalpha.entity;

public class ActivityData {
    private String activityId;
    private String activityName;
    private String activityDescription;
    private String activityTheme;
    private String activityStartTime;
    private String activityEndTime;
    private String activityAddress;
    private String imageUrl;
    private Integer maxCount;
    private Integer productCount;
    private Integer memberCount;

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
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

    public String getActivityTheme() {
        return activityTheme;
    }

    public void setActivityTheme(String activityTheme) {
        this.activityTheme = activityTheme;
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

    public String getActivityAddress() {
        return activityAddress;
    }

    public void setActivityAddress(String activityAddress) {
        this.activityAddress = activityAddress;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(Integer maxCount) {
        this.maxCount = maxCount;
    }

    public Integer getProductCount() {
        return productCount;
    }

    public void setProductCount(Integer productCount) {
        this.productCount = productCount;
    }

    public Integer getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(Integer memberCount) {
        this.memberCount = memberCount;
    }

    @Override
    public String toString() {
        return "ActivityData{" +
                "activityId='" + activityId + '\'' +
                ", activityName='" + activityName + '\'' +
                ", activityDescription='" + activityDescription + '\'' +
                ", activityTheme='" + activityTheme + '\'' +
                ", activityStartTime='" + activityStartTime + '\'' +
                ", activityEndTime='" + activityEndTime + '\'' +
                ", activityAddress='" + activityAddress + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", maxCount=" + maxCount +
                ", productCount=" + productCount +
                ", memberCount=" + memberCount +
                '}';
    }
}
