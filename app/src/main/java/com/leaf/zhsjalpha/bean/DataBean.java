package com.leaf.zhsjalpha.bean;

import java.util.ArrayList;
import java.util.List;

public class DataBean {
    public String imageUrl;
    public String title;
    public int msgType;

    public DataBean(String imageUrl, String title, int msgType) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.msgType = msgType;
    }

    public static List<DataBean> getTestData3() {
        List<DataBean> list = new ArrayList<>();
        list.add(new DataBean("https://pic.baixiongz.com/2020/08/30/c8c076202a693.jpg", null, 1));
        list.add(new DataBean("https://pic.baixiongz.com/2020/08/30/cc24589539159.jpg", null, 1));
        list.add(new DataBean("https://pic.baixiongz.com/2020/08/30/1b54b5c00d85f.jpg", null, 1));
        return list;
    }
}
