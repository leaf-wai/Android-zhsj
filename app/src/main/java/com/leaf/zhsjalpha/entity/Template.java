package com.leaf.zhsjalpha.entity;

public class Template {
    private String templateId;
    private String content;
    private Integer currencyId;
    private String currencyName;
    private String subcurrencyId;
    private String subcurrencyName;
    private Integer maxScore;
    private Integer courseId;
    private Integer target;

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getSubcurrencyId() {
        return subcurrencyId;
    }

    public void setSubcurrencyId(String subcurrencyId) {
        this.subcurrencyId = subcurrencyId;
    }

    public String getSubcurrencyName() {
        return subcurrencyName;
    }

    public void setSubcurrencyName(String subcurrencyName) {
        this.subcurrencyName = subcurrencyName;
    }

    public Integer getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(Integer maxScore) {
        this.maxScore = maxScore;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public Integer getTarget() {
        return target;
    }

    public void setTarget(Integer target) {
        this.target = target;
    }
}
