package com.beihui.market.util;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateFormatUtils {

    private static SimpleDateFormat newsDateFormat;
    private static SimpleDateFormat MMddHHmmFormat;
    private static Date date;
    private static Calendar dateCal;
    private static Calendar nowCal;

    public static String generateNewsDate(long gmt) {
        checkNewsField();
        long now = System.currentTimeMillis();
        if (gmt >= now - 5 * 1000 * 60) {
            //5分钟之内
            return "刚刚";
        } else if (gmt >= now - 60 * 1000 * 60) {
            //一个小时之内
            return ((now - gmt) / (1000 * 60)) + "分钟前";
        }
        dateCal.setTimeInMillis(gmt);
        nowCal.setTimeInMillis(now);
        int dateDay = dateCal.get(Calendar.DAY_OF_YEAR);
        int nowDay = nowCal.get(Calendar.DAY_OF_YEAR);
        if (dateDay == nowDay) {
            //同一天
            return ((now - gmt) / (1000 * 60 * 60)) + "小时前";
        } else if (dateDay == nowDay - 1) {
            //隔天
            return "昨天";
        }
        date.setTime(gmt);
        return getCN_MDFormat().format(date);
    }

    private static void checkNewsField() {
        if (date == null) {
            date = new Date();
        }
        if (dateCal == null) {
            dateCal = Calendar.getInstance(Locale.CHINA);
        }
        if (nowCal == null) {
            nowCal = Calendar.getInstance(Locale.CHINA);
        }
    }

    public static SimpleDateFormat getCN_MDFormat() {
        if (newsDateFormat == null) {
            newsDateFormat = new SimpleDateFormat("MM月dd日", Locale.CHINA);
        }
        return newsDateFormat;
    }

    public static String formatMMddHHmm(long timestamp) {
        if (MMddHHmmFormat == null) {
            MMddHHmmFormat = new SimpleDateFormat("MM月dd日 HH:mm", Locale.CHINA);
        }
        return MMddHHmmFormat.format(new Date(timestamp));
    }

}
