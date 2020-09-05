package com.leaf.zhsjalpha.entity;

public class Article {
    private int id;
    private int articleImageID;
    private String title;
    private String content;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getArticleImageID() {
        return articleImageID;
    }

    public void setArticleImageID(int articleImageID) {
        this.articleImageID = articleImageID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
