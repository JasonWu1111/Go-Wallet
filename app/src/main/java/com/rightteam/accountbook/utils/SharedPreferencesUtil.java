package com.rightteam.accountbook.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.rightteam.accountbook.MyApplication;

/**
 * Created by JasonWu on 2017/9/18
 */

public class SharedPreferencesUtil {

    private SharedPreferences mPrefs;

    private SharedPreferencesUtil() {
        mPrefs = MyApplication.getContext().getSharedPreferences("my_pref", Context.MODE_PRIVATE);
    }

    public static SharedPreferencesUtil getInstance() {
        return SPHolder.sInstance;
    }

    public void putInt(String key, int value) {
        mPrefs.edit().putInt(key, value).apply();
    }

    public int getInt(String key, int defValue) {
        return mPrefs.getInt(key, defValue);
    }

    public SharedPreferencesUtil putString(String key, String value) {
        mPrefs.edit().putString(key, value).apply();
        return this;
    }

    public String getString(String key, String defValue) {
        return mPrefs.getString(key, defValue);
    }

    public void putBoolean(String key, boolean value) {
        mPrefs.edit().putBoolean(key, value).apply();
    }

    public boolean getBoolean(String key, boolean defValue) {
        return mPrefs.getBoolean(key, defValue);
    }

    public void putFloat(String key, float value) {
        mPrefs.edit().putFloat(key, value).apply();
    }

    public float getFloat(String key, float defValue) {
        return mPrefs.getFloat(key, defValue);
    }

    public void putLong(String key, long value) {
        mPrefs.edit().putLong(key, value).apply();
    }

    public Long getLong(String key, long defValue) {
        return mPrefs.getLong(key, defValue);
    }

    public void clearUserData() {
        mPrefs.edit().clear().apply();
    }

    private static class SPHolder {
        private static final SharedPreferencesUtil sInstance = new SharedPreferencesUtil();
    }
}
