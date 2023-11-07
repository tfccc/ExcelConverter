package com.example.excelconverter.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Frank.Tang
 * @date 2023-11-07 14:36
 * @desc
 **/
public class DateUtil {

    public static final String YEAR_MONTH_DAY_HOUR = "yyyy-MM-dd HH:mm:ss";
    public static final String YEAR_MONTH_DAY_HOUR_0 = "yyyy-MM-dd_HH-mm-ss";
    public static final String YEAR_MONTH_DAY_HOUR_CN = "yyyy年MM月dd日HH时mm分ss秒";


    public static String getChineseFullDate() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat(YEAR_MONTH_DAY_HOUR_CN);
        return format.format(date);
    }

    public static String getDateStr(String formatStr) {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        return format.format(date);
    }
}
