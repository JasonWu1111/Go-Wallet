package com.rightteam.accountbook.utils;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by wuzhiqiang on 2018/7/29
 */
public class DensityUtil {

    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, context.getResources().getDisplayMetrics());

    }
}
