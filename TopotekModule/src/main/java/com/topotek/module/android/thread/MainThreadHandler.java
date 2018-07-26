package com.topotek.module.android.thread;

import android.os.Handler;
import android.os.Looper;

//ok
public class MainThreadHandler {

    private static Handler mainThreadHandler = new Handler(Looper.getMainLooper());
    /**
     * 不检查参数
     */
    public static void post(Runnable runnable) {
        mainThreadHandler.post(runnable);
    }

    public static void postDelayed(Runnable runnable, long delayMillis){
        mainThreadHandler.postDelayed(runnable, delayMillis);
    }
}
