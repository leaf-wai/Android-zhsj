package com.leaf.zhsjalpha.entity;

public class Processes {
    private String processId;
    private String activityId;
    private String processType;
    private String processDetail;
    private String ruleDetail;
    private Integer termId;
    private String createTime;
    private String updateTime;

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getProcessType() {
        return processType;
    }

    public void setProcessType(String processType) {
        this.processType = processType;
    }

    public String getProcessDetail() {
        return processDetail;
    }

    public void setProcessDetail(String processDetail) {
        this.processDetail = processDetail;
    }

    public String getRuleDetail() {
        return ruleDetail;
    }

    public void setRuleDetail(String ruleDetail) {
        this.ruleDetail = ruleDetail;
    }

    public Integer getTermId() {
        return termId;
    }

    public void setTermId(Integer termId) {
        this.termId = termId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "ProcessesList{" +
                "processId='" + processId + '\'' +
                ", activityId='" + activityId + '\'' +
                ", processType='" + processType + '\'' +
                ", processDetail='" + processDetail + '\'' +
                ", ruleDetail='" + ruleDetail + '\'' +
                ", termId=" + termId +
                ", createTime='" + createTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                '}';
    }
}
