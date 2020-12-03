package com.leaf.zhsjalpha.entity;

import java.util.List;

public class TeamData {

    private String teamId;
    private String processId;
    private String teamName;
    private String teamType;
    private String contactId;
    private String parentMen;
    private String parentWomen;
    private String contactName;
    private List<ActivityUser> activityUsers;

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamType() {
        return teamType;
    }

    public void setTeamType(String teamType) {
        this.teamType = teamType;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getParentMen() {
        return parentMen;
    }

    public void setParentMen(String parentMen) {
        this.parentMen = parentMen;
    }

    public String getParentWomen() {
        return parentWomen;
    }

    public void setParentWomen(String parentWomen) {
        this.parentWomen = parentWomen;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public List<ActivityUser> getActivityUsers() {
        return activityUsers;
    }

    public void setActivityUsers(List<ActivityUser> activityUsers) {
        this.activityUsers = activityUsers;
    }
}
