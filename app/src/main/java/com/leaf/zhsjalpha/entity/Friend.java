package com.leaf.zhsjalpha.entity;

public class Friend {
    private String name;
    private String sex;
    private String picUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    @Override
    public String toString() {
        return "Friend{" +
                "name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", picUrl='" + picUrl + '\'' +
                '}';
    }
}
