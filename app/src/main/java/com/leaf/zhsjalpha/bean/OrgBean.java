package com.leaf.zhsjalpha.bean;

import com.contrarywind.interfaces.IPickerViewData;

import java.util.List;

public class OrgBean implements IPickerViewData {
    private String name;
    private List<OrgBean.CityBean> city;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<OrgBean.CityBean> getCity() {
        return city;
    }

    public void setCity(List<OrgBean.CityBean> city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "OrgBean{" +
                "name='" + name + '\'' +
                ", city=" + city.toString() +
                '}';
    }

    // 实现 IPickerViewData 接口，
    // 这个用来显示在PickerView上面的字符串，
    // PickerView会通过IPickerViewData获取getPickerViewText方法显示出来。
    @Override
    public String getPickerViewText() {
        return this.name;
    }


    public static class CityBean {
        private String name;
        private List<String> area;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<String> getArea() {
            return area;
        }

        public void setArea(List<String> area) {
            this.area = area;
        }

        @Override
        public String toString() {
            return "CityBean{" +
                    "name='" + name + '\'' +
                    ", org=" + area +
                    '}';
        }
    }
}
