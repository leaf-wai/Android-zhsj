package com.leaf.zhsjalpha.entity;

public class ActivityInfoEntity {
    private String id;
    private String activityName;
    private String activityTheme;
    private String activityDescription;
    private String startTime;
    private String endTime;
    private String activityStartTime;
    private String activityEndTime;
    private String activityAddress;
    private String imageLong;
    private Integer contestantCount;
    private Integer coachCount;
    private String createTime;
    private String updateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getActivityTheme() {
        return activityTheme;
    }

    public void setActivityTheme(String activityTheme) {
        this.activityTheme = activityTheme;
    }

    public String getActivityDescription() {
        return activityDescription;
    }

    public void setActivityDescription(String activityDescription) {
        this.activityDescription = activityDescription;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
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

    public String getImageLong() {
        return imageLong;
    }

    public void setImageLong(String imageLong) {
        this.imageLong = imageLong;
    }

    public Integer getContestantCount() {
        return contestantCount;
    }

    public void setContestantCount(Integer contestantCount) {
        this.contestantCount = contestantCount;
    }

    public Integer getCoachCount() {
        return coachCount;
    }

    public void setCoachCount(Integer coachCount) {
        this.coachCount = coachCount;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "ActivityInfoEntity{" +
                "id='" + id + '\'' +
                ", activityName='" + activityName + '\'' +
                ", activityTheme='" + activityTheme + '\'' +
                ", activityDescription='" + activityDescription + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", activityStartTime='" + activityStartTime + '\'' +
                ", activityEndTime='" + activityEndTime + '\'' +
                ", activityAddress='" + activityAddress + '\'' +
                ", imageLong='" + imageLong + '\'' +
                ", contestantCount=" + contestantCount +
                ", coachCount=" + coachCount +
                ", createTime='" + createTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                '}';
    }
}
