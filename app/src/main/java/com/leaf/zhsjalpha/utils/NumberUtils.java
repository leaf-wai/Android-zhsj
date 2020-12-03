package com.leaf.zhsjalpha.utils;

import android.content.Context;

import com.leaf.zhsjalpha.bean.GradeBean;

import java.util.List;
import java.util.regex.Pattern;

public class NumberUtils {

    public static String getGradeName(Integer gradeId, Context context) {
        String gradeName;
        List<GradeBean> gradeBeanList;
        String JsonData = JsonUtils.getJson(context, "Grade.json");
        gradeBeanList = JsonUtils.parseGradeData(JsonData);
        gradeName = "未知年级";
        for (GradeBean gradeBean : gradeBeanList) {
            if (gradeId.equals(gradeBean.getGradeId()))
                gradeName = gradeBean.getGradeName();
        }
        return gradeName;
    }

    public static String getWeekName(int week) {
        switch (week) {
            case 1:
                return "星期一";
            case 2:
                return "星期二";
            case 3:
                return "星期三";
            case 4:
                return "星期四";
            case 5:
                return "星期五";
            case 6:
                return "星期六";
            case 7:
                return "星期日";
            default:
                return "未知";
        }
    }

    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

}
