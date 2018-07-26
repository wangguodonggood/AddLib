package com.topotek.libs.libs0.android.sharedPreferences;

import android.content.Context;

//ok
public class SPH extends SPHelper {

    private String name;

    public SPH(Context context, String name) {
        super(context);
        this.name = name;
    }

    public boolean putBoolean(String key, boolean value) {
        return putBoolean(name, key, value);
    }

    public boolean putInt(String key, int value) {
        return putInt(name, key, value);
    }

    public boolean putLong(String key, long value) {
        return putLong(name, key, value);
    }

    public boolean putFloat(String key, float value) {
        return putFloat(name, key, value);
    }

    public boolean putString(String key, String value) {
        return putString(name, key, value);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return getBoolean(name, key, defaultValue);
    }

    public int getInt(String key, int defaultValue) {
        return getInt(name, key, defaultValue);
    }

    public long getLong(String key, long defaultValue) {
        return getLong(name, key, defaultValue);
    }

    public float getFloat(String key, float defaultValue) {
        return getFloat(name, key, defaultValue);
    }

    public String getString(String key, String defaultValue) {
        return getString(name, key, defaultValue);
    }
}
