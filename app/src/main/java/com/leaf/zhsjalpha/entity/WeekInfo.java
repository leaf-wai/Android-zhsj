package com.leaf.zhsjalpha.entity;

public class WeekInfo {
    private Integer orgId;
    private String termId;
    private Integer academicYear;
    private String academicName;
    private String termStartTime;
    private String termEndTime;
    private Integer currentWeek;
    private Integer allWeeks;

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public String getTermId() {
        return termId;
    }

    public void setTermId(String termId) {
        this.termId = termId;
    }

    public Integer getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(Integer academicYear) {
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

    public Integer getCurrentWeek() {
        return currentWeek;
    }

    public void setCurrentWeek(Integer currentWeek) {
        this.currentWeek = currentWeek;
    }

    public Integer getAllWeeks() {
        return allWeeks;
    }

    public void setAllWeeks(Integer allWeeks) {
        this.allWeeks = allWeeks;
    }
}
