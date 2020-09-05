package com.leaf.zhsjalpha.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DataBean {
    public Integer imageRes;
    public String imageUrl;
    public String title;
    public int msgType;

    public DataBean(String title, int msgType) {
        this.title = title;
        this.msgType = msgType;
    }

    public DataBean(String imageUrl, String title, int msgType) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.msgType = msgType;
    }

    public static List<DataBean> getTestData2() {
        List<DataBean> list = new ArrayList<>();
        list.add(new DataBean("您的少儿编程课已成功报名", 1));
        list.add(new DataBean("您报名的PCA马术夏令营地点变更", 2));
        list.add(new DataBean("您的订单付款成功", 3));
        return list;
    }

    public static List<DataBean> getTestData3() {
        List<DataBean> list = new ArrayList<>();
        list.add(new DataBean("https://pic.baixiongz.com/2020/08/30/c8c076202a693.jpg", null, 1));
        list.add(new DataBean("https://pic.baixiongz.com/2020/08/30/cc24589539159.jpg", null, 1));
        list.add(new DataBean("https://pic.baixiongz.com/2020/08/30/1b54b5c00d85f.jpg", null, 1));
        return list;
    }

    public static List<String> getColors(int size) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(getRandColor());
        }
        return list;
    }

    /**
     * 获取十六进制的颜色代码.例如  "#5A6677"
     * 分别取R、G、B的随机值，然后加起来即可
     *
     * @return String
     */
    public static String getRandColor() {
        String R, G, B;
        Random random = new Random();
        R = Integer.toHexString(random.nextInt(256)).toUpperCase();
        G = Integer.toHexString(random.nextInt(256)).toUpperCase();
        B = Integer.toHexString(random.nextInt(256)).toUpperCase();

        R = R.length() == 1 ? "0" + R : R;
        G = G.length() == 1 ? "0" + G : G;
        B = B.length() == 1 ? "0" + B : B;

        return "#" + R + G + B;
    }
}
