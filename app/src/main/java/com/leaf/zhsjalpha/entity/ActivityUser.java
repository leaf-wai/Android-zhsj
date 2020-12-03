package com.leaf.zhsjalpha.entity;

public class ActivityUser {
    private String userId;
    private String processId;
    private String userName;
    private String userSex;
    private String userIdCard;
    private String userRace;
    private Integer gradeId;
    private String wx;
    private String tel;
    private Integer isCaptain;
    private Integer isCoach;
    private String createTime;
    private String updateTime;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserSex() {
        return userSex;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }

    public String getUserIdCard() {
        return userIdCard;
    }

    public void setUserIdCard(String userIdCard) {
        this.userIdCard = userIdCard;
    }

    public String getUserRace() {
        return userRace;
    }

    public void setUserRace(String userRace) {
        this.userRace = userRace;
    }

    public Integer getGradeId() {
        return gradeId;
    }

    public void setGradeId(Integer gradeId) {
        this.gradeId = gradeId;
    }

    public String getWx() {
        return wx;
    }

    public void setWx(String wx) {
        this.wx = wx;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public Integer getIsCaptain() {
        return isCaptain;
    }

    public void setIsCaptain(Integer isCaptain) {
        this.isCaptain = isCaptain;
    }

    public Integer getIsCoach() {
        return isCoach;
    }

    public void setIsCoach(Integer isCoach) {
        this.isCoach = isCoach;
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
        return "ActivityUser{" +
                "userId='" + userId + '\'' +
                ", processId='" + processId + '\'' +
                ", userName='" + userName + '\'' +
                ", userSex='" + userSex + '\'' +
                ", userIdCard='" + userIdCard + '\'' +
                ", userRace='" + userRace + '\'' +
                ", gradeId=" + gradeId +
                ", wx='" + wx + '\'' +
                ", tel='" + tel + '\'' +
                ", isCaptain=" + isCaptain +
                ", isCoach=" + isCoach +
                ", createTime='" + createTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                '}';
    }
}
