package com.topotek.module.java.threadExecutor;

import android.support.annotation.NonNull;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class SynchronizedExecutor {

    private static ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    public static ScheduledExecutorService getExecutor() {
        return executor;
    }

    public static void execute(@NonNull Runnable runnable) {
        executor.execute(runnable);
    }

    /**
     * 不检查参数
     */
    public static ScheduledFuture<?> schedule(@NonNull Runnable runnable, long delay, @NonNull TimeUnit unit) {
        return executor.schedule(runnable, delay, unit);
    }

    /**
     * 不检查参数
     */
    public static ScheduledFuture<?> scheduleWithFixedDelay(@NonNull Runnable runnable, long initialDelay, long delay, @NonNull TimeUnit unit) {
        return executor.scheduleWithFixedDelay(runnable, initialDelay, delay, unit);
    }
}
