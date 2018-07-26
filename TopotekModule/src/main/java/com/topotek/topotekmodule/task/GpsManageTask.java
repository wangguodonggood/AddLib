package com.topotek.topotekmodule.task;

import android.graphics.Color;

import com.topotek.module.android.thread.MainThreadHandler;
import com.topotek.module.java.threadExecutor.ThreadPool;
import com.topotek.topotekmodule.uiModule.UiModule;

import java.util.concurrent.TimeUnit;

public class GpsManageTask {

    private static int updataLongitudeSign = -1;
    private static int updataLatitudeSign = -1;

    static {
        ThreadPool.schedule(new Runnable() {
            @Override
            public void run() {
                switch (updataLongitudeSign) {
                    case 1:
                        updataLongitudeSign = 0;
                        break;
                    case 0:
                        MainThreadHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                UiModule.uiLayout.setGpsLongitudeTextColor(Color.YELLOW);
                            }
                        });
                        updataLongitudeSign = -1;
                        break;
                }

                switch (updataLatitudeSign) {
                    case 1:
                        updataLatitudeSign = 0;
                        break;
                    case 0:
                        MainThreadHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                UiModule.uiLayout.setGpsLatitudeTextColor(Color.YELLOW);
                            }
                        });
                        updataLatitudeSign = -1;
                        break;
                }

                ThreadPool.schedule(this, 4, TimeUnit.SECONDS);
            }
        }, 4, TimeUnit.SECONDS);
    }

    public static void updataLongitude(final String longitude) {
        if (updataLongitudeSign == -1) {
            MainThreadHandler.post(new Runnable() {
                @Override
                public void run() {
                    UiModule.uiLayout.setGpsLongitudeTextColor(Color.GREEN);
                }
            });
        }
        updataLongitudeSign = 1;
        MainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                UiModule.uiLayout.setGpsLongitudeText(longitude);
            }
        });
    }

    public static void updataLatitude(final String latitude) {
        if (updataLatitudeSign == -1) {
            MainThreadHandler.post(new Runnable() {
                @Override
                public void run() {
                    UiModule.uiLayout.setGpsLatitudeTextColor(Color.GREEN);
                }
            });
        }
        updataLatitudeSign = 1;
        MainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                UiModule.uiLayout.setGpsLatitudeText(latitude);
            }
        });
    }
}
