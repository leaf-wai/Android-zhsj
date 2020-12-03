package com.leaf.zhsjalpha.entity;

import java.util.List;

public class DataList<T> {
    private List<T> data;
    private Integer count;
    private Integer totalnum;
    private Integer pindex;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getTotalnum() {
        return totalnum;
    }

    public void setTotalnum(Integer totalnum) {
        this.totalnum = totalnum;
    }

    public Integer getPindex() {
        return pindex;
    }

    public void setPindex(Integer pindex) {
        this.pindex = pindex;
    }

    @Override
    public String toString() {
        return "DataList{" +
                "data=" + data +
                ", count=" + count +
                ", totalnum=" + totalnum +
                ", pindex=" + pindex +
                '}';
    }
}
