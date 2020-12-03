package com.leaf.zhsjalpha.entity;

import java.util.List;

public class Radar {
    private List<Statistic> statisticsEntityList;
    private Integer week;

    public List<Statistic> getStatisticsEntityList() {
        return statisticsEntityList;
    }

    public void setStatisticsEntityList(List<Statistic> statisticsEntityList) {
        this.statisticsEntityList = statisticsEntityList;
    }

    public Integer getWeek() {
        return week;
    }

    public void setWeek(Integer week) {
        this.week = week;
    }
}
