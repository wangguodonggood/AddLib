package com.topotek.module.java.threadExecutor;

import android.support.annotation.NonNull;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

//ok
public class FunnelExecutor {


    //创建线程池
    private static ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    public static ScheduledExecutorService getExecutor() {
        return executor;
    }
    //可以执行某个runnable对象
    public static void execute(@NonNull Runnable runnable) {
        executor.execute(runnable);
    }

    //scheduleAtFixedRate()方法实现周期性执行
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
