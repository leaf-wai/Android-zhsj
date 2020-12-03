package com.leaf.zhsjalpha.entity;

public class MyProduct {
    private String postId;
    private String postImageUrl;
    private String postTitle;
    private String postContent;
    private String postBuildTime;
    private int thumbUpNumber;
    private int commentNumber;

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostImageUrl() {
        return postImageUrl;
    }

    public void setPostImageUrl(String postImageUrl) {
        this.postImageUrl = postImageUrl;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public String getPostBuildTime() {
        return postBuildTime;
    }

    public void setPostBuildTime(String postBuildTime) {
        this.postBuildTime = postBuildTime;
    }

    public int getThumbUpNumber() {
        return thumbUpNumber;
    }

    public void setThumbUpNumber(int thumbUpNumber) {
        this.thumbUpNumber = thumbUpNumber;
    }

    public int getCommentNumber() {
        return commentNumber;
    }

    public void setCommentNumber(int commentNumber) {
        this.commentNumber = commentNumber;
    }
}
