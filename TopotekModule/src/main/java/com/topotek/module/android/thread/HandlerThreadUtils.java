package com.topotek.module.android.thread;

import android.os.HandlerThread;
import android.os.Looper;

//ok
public class HandlerThreadUtils {

    /**
     * 不检测参数
     */
    public static Looper startNewHandlerThread(String handlerThreadName) {
        HandlerThread handlerThread = new HandlerThread(handlerThreadName);
        handlerThread.start();
        return handlerThread.getLooper();
    }
}
