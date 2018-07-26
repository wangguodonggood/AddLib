package com.topotek.libs.libs0.android.sharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

//ok
public class SharedPreferencesUtils {

    public static SharedPreferences.Editor putBoolean(Context context, String name, String key, boolean value) {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE).edit().putBoolean(key, value);
    }

    public static SharedPreferences.Editor putInt(Context context, String name, String key, int value) {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE).edit().putInt(key, value);
    }

    public static SharedPreferences.Editor putLong(Context context, String name, String key, long value) {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE).edit().putLong(key, value);
    }

    public static SharedPreferences.Editor putFloat(Context context, String name, String key, float value) {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE).edit().putFloat(key, value);
    }

    public static SharedPreferences.Editor putString(Context context, String name, String key, String value) {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE).edit().putString(key, value);
    }

    public static boolean getBoolean(Context context, String name, String key, boolean defaultValue) {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE).getBoolean(key, defaultValue);
    }

    public static int getInt(Context context, String name, String key, int defaultValue) {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE).getInt(key, defaultValue);
    }

    public static long getLong(Context context, String name, String key, long defaultValue) {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE).getLong(key, defaultValue);
    }

    public static float getFloat(Context context, String name, String key, float defaultValue) {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE).getFloat(key, defaultValue);
    }

    public static String getString(Context context, String name, String key, String defaultValue) {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE).getString(key, defaultValue);
    }
}
