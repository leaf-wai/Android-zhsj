package com.leaf.zhsjalpha.entity;

public class MyCourse {
    private String classId;
    private String courseName;
    private String courseImageUrl;

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseImageUrl() {
        return courseImageUrl;
    }

    public void setCourseImageUrl(String courseImageUrl) {
        this.courseImageUrl = courseImageUrl;
    }

    @Override
    public String toString() {
        return "MyCourse{" +
                "classId='" + classId + '\'' +
                ", courseName='" + courseName + '\'' +
                ", courseImageUrl='" + courseImageUrl + '\'' +
                '}';
    }
}
