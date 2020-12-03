package com.leaf.zhsjalpha.entity;

public class Organization {
    private int orgId;
    private String orgName;
    private String provinceName;
    private String cityName;
    private String createTime;
    private String termId;
    private int academicYear;
    private String academicName;
    private String termStartTime;
    private String termEndTime;
    private String prompt;

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getTermId() {
        return termId;
    }

    public void setTermId(String termId) {
        this.termId = termId;
    }

    public int getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(int academicYear) {
        this.academicYear = academicYear;
    }

    public String getAcademicName() {
        return academicName;
    }

    public void setAcademicName(String academicName) {
        this.academicName = academicName;
    }

    public String getTermStartTime() {
        return termStartTime;
    }

    public void setTermStartTime(String termStartTime) {
        this.termStartTime = termStartTime;
    }

    public String getTermEndTime() {
        return termEndTime;
    }

    public void setTermEndTime(String termEndTime) {
        this.termEndTime = termEndTime;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    @Override
    public String toString() {
        return "Organization{" +
                "orgId=" + orgId +
                ", orgName='" + orgName + '\'' +
                ", provinceName='" + provinceName + '\'' +
                ", cityName='" + cityName + '\'' +
                ", createTime='" + createTime + '\'' +
                ", termId='" + termId + '\'' +
                ", academicYear=" + academicYear +
                ", academicName='" + academicName + '\'' +
                ", termStartTime='" + termStartTime + '\'' +
                ", termEndTime='" + termEndTime + '\'' +
                ", prompt='" + prompt + '\'' +
                '}';
    }
}
