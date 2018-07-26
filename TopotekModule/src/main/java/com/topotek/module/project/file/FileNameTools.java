package com.topotek.module.project.file;

import android.os.SystemClock;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

//ok
public class FileNameTools {

    private static SimpleDateFormat elapsedRealtimeFormat = new SimpleDateFormat("mmss", Locale.getDefault());

    static {
        String[] availableIDs = TimeZone.getAvailableIDs(0);
        TimeZone timeZone = TimeZone.getTimeZone(availableIDs[0]);
        elapsedRealtimeFormat.setTimeZone(timeZone);
    }

    /**
     * @return 格式化的系统已运行时间：时mmss    if 时<10 则前补0    时最大为Long.MAX_VALUE
     */
    public static String elapsedRealtimeFormat() {

        long time = SystemClock.elapsedRealtime();

        long hour = time / 3600000;//  3600000 = 1000ms * 1 * 60 * 60  一小时的毫秒
        String h;
        if (hour < 10)
            h = "0" + hour;
        else
            h = String.valueOf(hour);

        Date date = new Date(time);
        String mmss = elapsedRealtimeFormat.format(date);

        return h + mmss;
    }
}
