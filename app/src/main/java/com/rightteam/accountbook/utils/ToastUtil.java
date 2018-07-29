package com.rightteam.accountbook.utils;

import android.widget.Toast;

import com.rightteam.accountbook.MyApplication;

/**
 * Created by wuzhiqiang on 2018/7/29
 */
public class ToastUtil {
    public static void showToast(String msg){
        Toast.makeText(MyApplication.getContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
