package com.topotek.module.java.threadExecutor;

import android.support.annotation.NonNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

//ok
public class ThreadPool {

    private static ExecutorService executor = Executors.newFixedThreadPool(5);

    public static ExecutorService getExecutor() {
        return executor;
    }

    public static void execute(@NonNull Runnable runnable) {
        executor.execute(runnable);
    }

    /**
     * 不检查参数
     */
    public static ScheduledFuture<?> schedule(@NonNull final Runnable runnable, long delay, @NonNull TimeUnit unit) {
        return FunnelExecutor.schedule(new Runnable() {
            @Override
            public void run() {
                executor.execute(runnable);
            }
        }, delay, unit);
    }

//    /**
//     * 不检查参数
//     */
//    public static ScheduledFuture<?> scheduleWithFixedDelay(@NonNull final Runnable runnable, long initialDelay, long delay, @NonNull TimeUnit unit) {
//        return FunnelExecutor.scheduleWithFixedDelay(new Runnable() {
//            @Override
//            public void run() {
//                executor.execute(runnable);
//            }
//        }, initialDelay, delay, unit);
//    }
}
