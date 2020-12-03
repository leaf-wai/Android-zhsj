package com.leaf.zhsjalpha.entity;

import java.util.List;

public class CurrencyTypeData {
    private Integer orgId;
    private Integer currencyId;
    private String currencyName;
    private Integer subcurrencyId;
    private String subcurrencyName;
    private String contentList;
    private List<Template> templateList;

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
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

    public Integer getSubcurrencyId() {
        return subcurrencyId;
    }

    public void setSubcurrencyId(Integer subcurrencyId) {
        this.subcurrencyId = subcurrencyId;
    }

    public String getSubcurrencyName() {
        return subcurrencyName;
    }

    public void setSubcurrencyName(String subcurrencyName) {
        this.subcurrencyName = subcurrencyName;
    }

    public String getContentList() {
        return contentList;
    }

    public void setContentList(String contentList) {
        this.contentList = contentList;
    }

    public List<Template> getTemplateList() {
        return templateList;
    }

    public void setTemplateList(List<Template> templateList) {
        this.templateList = templateList;
    }

    @Override
    public String toString() {
        return "CurrencyTypeData{" +
                "orgId=" + orgId +
                ", currencyId=" + currencyId +
                ", currencyName='" + currencyName + '\'' +
                ", subcurrencyId=" + subcurrencyId +
                ", subcurrencyName='" + subcurrencyName + '\'' +
                ", contentList='" + contentList + '\'' +
                ", templateList=" + templateList +
                '}';
    }
}
