package com.rightteam.accountbook.Utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils {

    private static final String DEFAULT_DAY_PATTERN = "dd/MM/yyyy";
    private static final String WEEK_DAY_PATTERN = "EEEE, dd MMMM";

    public static String formatNumberWithComma(float value) {
        BigDecimal bigDecimal = new BigDecimal(value);
        value = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
        return decimalFormat.format(value);
    }

    public static String formatTimestamp(long time, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));

        return sdf.format(new Date(time));
    }

    public static String formatWithToday(long time, String pattern) {
        TimeZone usTimeZone = TimeZone.getTimeZone("America/Los_Angeles");

        Calendar calendar = Calendar.getInstance(usTimeZone);
        calendar.setTimeInMillis(time);

        Calendar today = Calendar.getInstance(usTimeZone);

        if (calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR)
                && calendar.get(Calendar.MONTH) == today.get(Calendar.MONTH)
                && calendar.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH)) {
            return "today";
        } else {
            return formatTimestamp(time, pattern);
        }
    }

    //时区都是以美国洛杉矶为准
    public static void main(String[] args) {
        //需求1：
        String num1 = formatNumberWithComma(234233.124f);
        String num2 = formatNumberWithComma(200);
        System.out.println("num1:" + num1 + "---num2:" + num2);


        //需求2：
        String day = formatTimestamp(System.currentTimeMillis(), DEFAULT_DAY_PATTERN);
        System.out.println(day);

        //需求3:
        String weekDay = formatWithToday(System.currentTimeMillis(), WEEK_DAY_PATTERN);
        System.out.println(weekDay);

    }
}
