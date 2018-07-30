package com.rightteam.accountbook.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class CommonUtils {

    public static final String DEFAULT_DAY_PATTERN = "MM/dd/yyyy";
    public static final String WEEK_DAY_PATTERN = "EEEE, MMMM dd";
    public static final String YEAR_MONTH_PATTERN = "yyyyMM";


    public static String formatNumberWithComma(float value) {
        BigDecimal bigDecimal = new BigDecimal(value);
        value = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
        return decimalFormat.format(value);
    }

    public static String formatTimestamp(long time, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.getDefault());
//        sdf.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        return sdf.format(new Date(time));
    }

    public static String formatWithToday(long time, String pattern) {
//        TimeZone usTimeZone = TimeZone.getTimeZone("America/Los_Angeles");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);

        Calendar today = Calendar.getInstance();

        if (calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR)
                && calendar.get(Calendar.MONTH) == today.get(Calendar.MONTH)
                && calendar.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH)) {
            return "TODAY";
        } else {
            return formatTimestamp(time, pattern);
        }
    }

    public static String formatPriceWithSource(float price, boolean isExpense){
        return (isExpense ? "-" : "+") + " $" + formatNumberWithComma(price);
    }

    public static String formatDateSimple(int year, int month, int dayOfMonth){
        String m = (month < 10 ? "0" : "")  + month;
        String d = (dayOfMonth < 10 ? "0" : "")  + dayOfMonth;
        return m + "/" + d + "/" + year;
    }

    public static String formatDateSimple(int year, int month){
        String m = (month < 10 ? "0" : "")  + month;
        return m + "/" + year;
    }


    public static long transformStartMonthToMillis(int year, int month){
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, 0);
        return calendar.getTimeInMillis();
    }

    public static long transformEndMonthToMillis(int year, int month){
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month + 1, 0);
        return calendar.getTimeInMillis() - 1;
    }
}