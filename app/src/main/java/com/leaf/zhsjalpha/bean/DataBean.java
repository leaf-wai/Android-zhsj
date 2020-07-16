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
        list.add(new DataBean("https://imgcps.jd.com/ling4/71252549854/6Iqx5qC36Zu26aOf/576O6aOf5aSn6LWP/p-5d91a4f642dd5b7c7d52cbe9/ee28af45/cr/s1125x690/q70.jpg", null, 1));
        list.add(new DataBean("https://m.360buyimg.com/mobilecms/s700x280_jfs/t1/113846/3/12262/54893/5f0d1717Ed7548ce9/54664a03d5812617.jpg!cr_1125x445_0_171!q70.jpg", null, 1));
        list.add(new DataBean("https://m.360buyimg.com/mobilecms/s700x280_jfs/t1/101007/37/19520/185765/5e9ebef6E8138018c/28d8bd0b60f026b0.jpg!cr_1125x445_0_171!q70.jpg", null, 1));
        list.add(new DataBean("https://m.360buyimg.com/mobilecms/s700x280_jfs/t1/118802/9/12527/152599/5f07d819E0c0246fd/76065ca5d976d605.jpg!cr_1125x445_0_171!q70.jpg", null, 1));
        list.add(new DataBean("https://m.360buyimg.com/mobilecms/s700x280_jfs/t1/123465/4/6903/138982/5f0ac189E51013d1d/934244f4f9300f37.jpg!cr_1125x445_0_171!q70.jpg", null, 1));
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
