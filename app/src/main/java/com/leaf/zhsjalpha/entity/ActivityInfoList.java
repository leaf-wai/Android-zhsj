package com.leaf.zhsjalpha.entity;

import java.util.List;

public class ActivityInfoList {
    private ActivityInfoEntity activityInfoEntity;
    private List<Teacher> teacherIdList;
    private List<Processes> processesList;

    public ActivityInfoEntity getActivityInfoEntity() {
        return activityInfoEntity;
    }

    public void setActivityInfoEntity(ActivityInfoEntity activityInfoEntity) {
        this.activityInfoEntity = activityInfoEntity;
    }

    public List<Teacher> getTeacherIdList() {
        return teacherIdList;
    }

    public void setTeacherIdList(List<Teacher> teacherIdList) {
        this.teacherIdList = teacherIdList;
    }

    public List<Processes> getProcessesList() {
        return processesList;
    }

    public void setProcessesList(List<Processes> processesList) {
        this.processesList = processesList;
    }

    @Override
    public String toString() {
        return "ActivityInfoList{" +
                "activityInfoEntity=" + activityInfoEntity +
                ", teacherIdList=" + teacherIdList +
                ", processesList=" + processesList +
                '}';
    }
}
