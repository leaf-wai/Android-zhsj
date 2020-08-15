package com.leaf.zhsjalpha.entity;

public class Work {
    private int id;
    private int workImageID;
    private String workCourse;
    private String Content;
    private String Deadline;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWorkImageID() {
        return workImageID;
    }

    public void setWorkImageID(int workImageID) {
        this.workImageID = workImageID;
    }

    public String getWorkCourse() {
        return workCourse;
    }

    public void setWorkCourse(String workCourse) {
        this.workCourse = workCourse;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getDeadline() {
        return Deadline;
    }

    public void setDeadline(String deadline) {
        Deadline = deadline;
    }
}
