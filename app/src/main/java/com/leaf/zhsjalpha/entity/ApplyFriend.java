package com.leaf.zhsjalpha.entity;

public class ApplyFriend {
    private String name;
    private String sex;
    private String picUrl;
    private String applyContent;
    private Integer id;

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

    public String getApplyContent() {
        return applyContent;
    }

    public void setApplyContent(String applyContent) {
        this.applyContent = applyContent;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ApplyFriend{" +
                "name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", picUrl='" + picUrl + '\'' +
                ", applyContent='" + applyContent + '\'' +
                ", id=" + id +
                '}';
    }
}
