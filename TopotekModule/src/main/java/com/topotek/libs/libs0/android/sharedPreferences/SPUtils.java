package com.topotek.libs.libs0.android.sharedPreferences;

import android.content.Context;

//ok
public class SPUtils {

    public static boolean putBoolean(Context context, String name, String key, boolean value) {
        return SharedPreferencesUtils.putBoolean(context, name, key, value).commit();
    }

    public static boolean putInt(Context context, String name, String key, int value) {
        return SharedPreferencesUtils.putInt(context, name, key, value).commit();
    }

    public static boolean putLong(Context context, String name, String key, long value) {
        return SharedPreferencesUtils.putLong(context, name, key, value).commit();
    }

    public static boolean putFloat(Context context, String name, String key, float value) {
        return SharedPreferencesUtils.putFloat(context, name, key, value).commit();
    }

    public static boolean putString(Context context, String name, String key, String value) {
        return SharedPreferencesUtils.putString(context, name, key, value).commit();
    }

    public static boolean getBoolean(Context context, String name, String key, boolean defaultValue) {
        return SharedPreferencesUtils.getBoolean(context, name, key, defaultValue);
    }

    public static int getInt(Context context, String name, String key, int defaultValue) {
        return SharedPreferencesUtils.getInt(context, name, key, defaultValue);
    }

    public static long getLong(Context context, String name, String key, long defaultValue) {
        return SharedPreferencesUtils.getLong(context, name, key, defaultValue);
    }

    public static float getFloat(Context context, String name, String key, float defaultValue) {
        return SharedPreferencesUtils.getFloat(context, name, key, defaultValue);
    }

    public static String getString(Context context, String name, String key, String defaultValue) {
        return SharedPreferencesUtils.getString(context, name, key, defaultValue);
    }
}
