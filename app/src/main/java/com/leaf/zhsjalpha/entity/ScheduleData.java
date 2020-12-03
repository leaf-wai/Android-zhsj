package com.leaf.zhsjalpha.entity;

import java.util.List;

public class ScheduleData {

    private List<StandardBean> standard;
    private List<?> schedule;

    public List<StandardBean> getStandard() {
        return standard;
    }

    public void setStandard(List<StandardBean> standard) {
        this.standard = standard;
    }

    public List<?> getSchedule() {
        return schedule;
    }

    public void setSchedule(List<?> schedule) {
        this.schedule = schedule;
    }

    public static class StandardBean {
        private Integer courseTimeId;
        private String startTime;
        private String endTime;

        public Integer getCourseTimeId() {
            return courseTimeId;
        }

        public void setCourseTimeId(Integer courseTimeId) {
            this.courseTimeId = courseTimeId;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }
    }
}
