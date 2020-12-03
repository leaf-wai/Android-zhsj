package com.leaf.zhsjalpha.entity;

public class Declare {
    private String content;
    private Integer score;
    private String submitTime;
    private String subcurrencyName;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(String submitTime) {
        this.submitTime = submitTime;
    }

    public String getSubcurrencyName() {
        return subcurrencyName;
    }

    public void setSubcurrencyName(String subcurrencyName) {
        this.subcurrencyName = subcurrencyName;
    }

    @Override
    public String toString() {
        return "Declare{" +
                "content='" + content + '\'' +
                ", score=" + score +
                ", submitTime='" + submitTime + '\'' +
                ", subcurrencyName='" + subcurrencyName + '\'' +
                '}';
    }
}
