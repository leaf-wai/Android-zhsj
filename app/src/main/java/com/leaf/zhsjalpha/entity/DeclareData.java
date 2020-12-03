package com.leaf.zhsjalpha.entity;

public class DeclareData {
    private String studentId;
    private String contentId;
    private String content;
    private Integer score;
    private String courseId;
    private Integer currencyId;
    private Integer subcurrencyId;
    private Integer week;
    private String submitTime;
    private String checkedTime;
    private String teacherId;
    private Integer checked;
    private Integer passed;
    private Integer yn;
    private String classId;
    private String templateCreatTime;
    private String period;
    private String deadline;
    private String resourceId;
    private Integer orgId;

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

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

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public Integer getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
    }

    public Integer getSubcurrencyId() {
        return subcurrencyId;
    }

    public void setSubcurrencyId(Integer subcurrencyId) {
        this.subcurrencyId = subcurrencyId;
    }

    public Integer getWeek() {
        return week;
    }

    public void setWeek(Integer week) {
        this.week = week;
    }

    public String getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(String submitTime) {
        this.submitTime = submitTime;
    }

    public String getCheckedTime() {
        return checkedTime;
    }

    public void setCheckedTime(String checkedTime) {
        this.checkedTime = checkedTime;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public Integer getChecked() {
        return checked;
    }

    public void setChecked(Integer checked) {
        this.checked = checked;
    }

    public Integer getPassed() {
        return passed;
    }

    public void setPassed(Integer passed) {
        this.passed = passed;
    }

    public Integer getYn() {
        return yn;
    }

    public void setYn(Integer yn) {
        this.yn = yn;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getTemplateCreatTime() {
        return templateCreatTime;
    }

    public void setTemplateCreatTime(String templateCreatTime) {
        this.templateCreatTime = templateCreatTime;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    @Override
    public String toString() {
        return "DeclareData{" +
                "studentId='" + studentId + '\'' +
                ", contentId='" + contentId + '\'' +
                ", content='" + content + '\'' +
                ", score=" + score +
                ", courseId='" + courseId + '\'' +
                ", currencyId=" + currencyId +
                ", subcurrencyId=" + subcurrencyId +
                ", week=" + week +
                ", submitTime='" + submitTime + '\'' +
                ", checkedTime='" + checkedTime + '\'' +
                ", teacherId='" + teacherId + '\'' +
                ", checked=" + checked +
                ", passed=" + passed +
                ", yn=" + yn +
                ", classId='" + classId + '\'' +
                ", templateCreatTime='" + templateCreatTime + '\'' +
                ", period='" + period + '\'' +
                ", deadline='" + deadline + '\'' +
                ", resourceId='" + resourceId + '\'' +
                ", orgId=" + orgId +
                '}';
    }
}
