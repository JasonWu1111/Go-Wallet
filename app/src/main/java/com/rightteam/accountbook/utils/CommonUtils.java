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
    public static final String WEEK_DAY_PATTERN = "EEEE, dd MMMM";

    public static String formatNumberWithComma(float value) {
        BigDecimal bigDecimal = new BigDecimal(value);
        value = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
        return decimalFormat.format(value);
    }

    public static String formatTimestamp(long time, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.US);
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
}