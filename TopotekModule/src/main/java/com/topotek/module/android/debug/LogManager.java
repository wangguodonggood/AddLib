package com.topotek.module.android.debug;

import android.util.Log;

/**
 * Log统一管理
 */
public class LogManager {

    public static boolean isDebug = false;

    /**
     * 不检查参数
     * <p>
     * error Log
     */
    public static void e(String tag, String msg) {

        if (!isDebug)
            return;

        Log.e(tag, msg);
    }

    /**
     * 不检查参数
     * <p>
     * error Log
     */
    public static void e(String msg) {

        if (!isDebug)
            return;

        StringBuilder stringBuilder = decorateMsg(msg);
        msg = stringBuilder.toString();

        e("DebugUtils_Log.e", msg);
    }

    /**
     * 不检查参数
     * <p>
     * 默认Log
     */
    public static void log(String msg) {

        if (!isDebug)
            return;

        e("DebugUtils_Log.log", msg);
    }

    /**
     * 未检查参数
     */
    private static StringBuilder decorateMsg(String msg) {

        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        StringBuilder stringBuilder = stackTraceElementsToString(stackTraceElements);

        stringBuilder.append("-----------------------------------------------------------------------------------\n");
        stringBuilder.append(msg);
        stringBuilder.append("\n-----------------------------------------------------------------------------------");

        return stringBuilder;
    }

    /**
     * 未检查参数
     */
    private static StringBuilder stackTraceElementsToString(StackTraceElement[] stackTraceElements) {

        StringBuilder stringBuilder = new StringBuilder();

        for (StackTraceElement stackTraceElement : stackTraceElements) {

            String string = stackTraceElement.toString();
            stringBuilder.append(string);
            stringBuilder.append("\n");
        }

        return stringBuilder;
    }
}
