package com.topotek.libs.libs0.android.sharedPreferences;

import android.content.Context;

//ok
public class SPHelper {

    private Context context;

    public SPHelper(Context context) {
        this.context = context;
    }

    public boolean putBoolean(String name, String key, boolean value) {
        return SPUtils.putBoolean(context, name, key, value);
    }

    public boolean putInt(String name, String key, int value) {
        return SPUtils.putInt(context, name, key, value);
    }

    public boolean putLong(String name, String key, long value) {
        return SPUtils.putLong(context, name, key, value);
    }

    public boolean putFloat(String name, String key, float value) {
        return SPUtils.putFloat(context, name, key, value);
    }

    public boolean putString(String name, String key, String value) {
        return SPUtils.putString(context, name, key, value);
    }

    public boolean getBoolean(String name, String key, boolean defaultValue) {
        return SPUtils.getBoolean(context, name, key, defaultValue);
    }

    public int getInt(String name, String key, int defaultValue) {
        return SPUtils.getInt(context, name, key, defaultValue);
    }

    public long getLong(String name, String key, long defaultValue) {
        return SPUtils.getLong(context, name, key, defaultValue);
    }

    public float getFloat(String name, String key, float defaultValue) {
        return SPUtils.getFloat(context, name, key, defaultValue);
    }

    public String getString(String name, String key, String defaultValue) {
        return SPUtils.getString(context, name, key, defaultValue);
    }
}
