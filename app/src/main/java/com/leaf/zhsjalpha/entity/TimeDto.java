package com.leaf.zhsjalpha.entity;

import java.util.List;

public class TimeDto {
    private List<String> week;
    private String courseStartDate;
    private String courseEndDate;
    private String courseStartTime;
    private String courseEndTime;
    private String regularClassRoom;

    public List<String> getWeek() {
        return week;
    }

    public void setWeek(List<String> week) {
        this.week = week;
    }

    public String getCourseStartDate() {
        return courseStartDate;
    }

    public void setCourseStartDate(String courseStartDate) {
        this.courseStartDate = courseStartDate;
    }

    public String getCourseEndDate() {
        return courseEndDate;
    }

    public void setCourseEndDate(String courseEndDate) {
        this.courseEndDate = courseEndDate;
    }

    public String getCourseStartTime() {
        return courseStartTime;
    }

    public void setCourseStartTime(String courseStartTime) {
        this.courseStartTime = courseStartTime;
    }

    public String getCourseEndTime() {
        return courseEndTime;
    }

    public void setCourseEndTime(String courseEndTime) {
        this.courseEndTime = courseEndTime;
    }

    public String getRegularClassRoom() {
        return regularClassRoom;
    }

    public void setRegularClassRoom(String regularClassRoom) {
        this.regularClassRoom = regularClassRoom;
    }
}
