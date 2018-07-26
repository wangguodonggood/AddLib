package com.topotek.module.android.system;

import android.app.AlarmManager;
import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;
import android.text.format.DateFormat;

import java.util.TimeZone;

public class Setting {

    public static void initSystemTimeSetting(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        //若使用网络获取时区则取消
        int autuTimeZone = Settings.Global.getInt(contentResolver, Settings.Global.AUTO_TIME_ZONE, 1);
        if (autuTimeZone > 0)
            Settings.Global.putInt(contentResolver, android.provider.Settings.Global.AUTO_TIME_ZONE, 0);
        //若使用网络获取时间则取消
        int autoTime = Settings.Global.getInt(contentResolver, Settings.Global.AUTO_TIME, 1);
        if (autoTime > 0)
            Settings.Global.putInt(contentResolver, Settings.Global.AUTO_TIME, 0);
        //若未使用24小时格式则使用
        if (!DateFormat.is24HourFormat(context))
            Settings.System.putString(contentResolver, Settings.System.TIME_12_24, "24");
    }

    public static void setSystemTimeZone(Context context, int rawOffset) {
        //获取当前时区的偏移量，跟要设置的偏移量相比较
        TimeZone currentTimeZone = TimeZone.getDefault();
        if (currentTimeZone.getRawOffset() != rawOffset) {
            //要设置的偏移量下的可用时区ID
            String[] availableTimeZoneIDs = TimeZone.getAvailableIDs(rawOffset);
            if (availableTimeZoneIDs != null && availableTimeZoneIDs.length > 0) {
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                //使用该偏移量可用的时区的第一个即可
                alarmManager.setTimeZone(availableTimeZoneIDs[0]);
            }
        }
    }

    public static void setSystemTime(Context context, long millis) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        //参数为距1970-1-1的毫秒数
        alarmManager.setTime(millis);
    }
}
