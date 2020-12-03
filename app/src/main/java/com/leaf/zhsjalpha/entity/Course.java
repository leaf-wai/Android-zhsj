package com.leaf.zhsjalpha.entity;

public class Course {
    private String classId;
    private String courseImgUrl;
    private String courseName;
    private String interestType;
    private String courseType;
    private Integer remain;
    private Float price;
    private String payEndTime;

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getCourseImgUrl() {
        return courseImgUrl;
    }

    public void setCourseImgUrl(String courseImgUrl) {
        this.courseImgUrl = courseImgUrl;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getInterestType() {
        return interestType;
    }

    public void setInterestType(String interestType) {
        this.interestType = interestType;
    }

    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }

    public Integer getRemain() {
        return remain;
    }

    public void setRemain(Integer remain) {
        this.remain = remain;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getPayEndTime() {
        return payEndTime;
    }

    public void setPayEndTime(String payEndTime) {
        this.payEndTime = payEndTime;
    }

    @Override
    public String toString() {
        return "Course{" +
                "classId='" + classId + '\'' +
                ", courseImgUrl='" + courseImgUrl + '\'' +
                ", courseName='" + courseName + '\'' +
                ", interestType='" + interestType + '\'' +
                ", courseType='" + courseType + '\'' +
                ", remain=" + remain +
                ", price=" + price +
                ", payEndTime='" + payEndTime + '\'' +
                '}';
    }
}
