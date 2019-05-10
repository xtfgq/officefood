package com.xxx.ency.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间转化工具类
 * Created by xiarh on 2017/9/14.
 */

public class DateUtil {

    /**
     * 毫秒值转化为时间
     *
     * @param time 时间毫秒
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String Long2String(long time) {
        Date date = new Date(time);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }

    /**
     * 时间转化为字符串
     *
     * @param date
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String Date2String(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(date);
    }

    /**
     * 字符串转化为时间
     *
     * @param time 2010-11-20 11:10:10
     * @return Date
     */
    public static Date String2Date(String time) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return formatter.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 将String型格式化,比如想要将2011-11-11格式化成2011年11月11日,就StringPattern("2011-11-11","yyyy-MM-dd","yyyy年MM月dd日").
     *
     * @param date       String 想要格式化的日期
     * @param oldPattern String 想要格式化的日期的现有格式
     * @param newPattern String 想要格式化成什么格式
     * @return String
     */
    public static String StringPattern(String date, String oldPattern, String newPattern) {
        if (date == null || oldPattern == null || newPattern == null)
            return "";
        SimpleDateFormat sdf1 = new SimpleDateFormat(oldPattern);        // 实例化模板对象
        SimpleDateFormat sdf2 = new SimpleDateFormat(newPattern);        // 实例化模板对象
        Date d = null;
        try {
            d = sdf1.parse(date);   // 将给定的字符串中的日期提取出来
        } catch (Exception e) {            // 如果提供的字符串格式有错误，则进行异常处理
            e.printStackTrace();       // 打印异常信息
        }
        return sdf2.format(d);
    }
}
