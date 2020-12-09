package com.leaf.zhsjalpha.entity;

import java.util.List;

public class Schedule {
    private String scheduleId;
    private String courseName;
    private Integer status;
    private String classroomName;
    private String courseDate;
    private String weekDay;
    private List<String> courseTimeId;

    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getClassroomName() {
        return classroomName;
    }

    public void setClassroomName(String classroomName) {
        this.classroomName = classroomName;
    }

    public String getCourseDate() {
        return courseDate;
    }

    public void setCourseDate(String courseDate) {
        this.courseDate = courseDate;
    }

    public String getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(String weekDay) {
        this.weekDay = weekDay;
    }

    public List<String> getCourseTimeId() {
        return courseTimeId;
    }

    public void setCourseTimeId(List<String> courseTimeId) {
        this.courseTimeId = courseTimeId;
    }
}
