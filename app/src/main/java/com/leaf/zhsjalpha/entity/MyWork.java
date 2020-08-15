package com.leaf.zhsjalpha.entity;

public class MyWork {
    private int id;
    private int workPicID;
    private int workImageID;
    private String workCourse;
    private String Content;
    private String myWorkName;
    private String myWorkContent;
    private int like;
    private int comment;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWorkPicID() {
        return workPicID;
    }

    public void setWorkPicID(int workPicID) {
        this.workPicID = workPicID;
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

    public String getMyWorkName() {
        return myWorkName;
    }

    public void setMyWorkName(String myWorkName) {
        this.myWorkName = myWorkName;
    }

    public String getMyWorkContent() {
        return myWorkContent;
    }

    public void setMyWorkContent(String myWorkContent) {
        this.myWorkContent = myWorkContent;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }
}
