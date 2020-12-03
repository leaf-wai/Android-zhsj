package com.leaf.zhsjalpha.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {
    public static String calTime(String time) {
        String timeDiff = "";
        Date nowTime = new Date();
        Date endTime = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            endTime = format.parse(time);

            long diff = nowTime.getTime() - endTime.getTime();

            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);

            if (diffDays != 0) {
                timeDiff = diffDays + " 天前";
            } else if (diffDays == 0 && diffHours != 0) {
                timeDiff = diffHours + " 小时前";
            } else if (diffHours == 0 && diffMinutes != 0) {
                timeDiff = diffMinutes + " 分钟前";
            } else if (diffMinutes == 0 && diffSeconds != 0) {
                timeDiff = diffSeconds + " 秒前";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return timeDiff;
    }

    public static String timeFormat(String time) {
        String timeFormat = "";
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat format1 = new SimpleDateFormat("HH:mm");
        try {
            Date date = format.parse(time);
            timeFormat = format1.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeFormat;
    }
}
