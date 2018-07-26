package com.topotek.movidius;

import android.app.Application;

import com.topotek.module.android.debug.Debugger;
import com.topotek.module.android.debug.LogManager;
import com.topotek.module.project.version.Version;

public class App extends Application implements Thread.UncaughtExceptionHandler {

    @Override
    public void onCreate() {
        super.onCreate();

        Version.modelCode = "SMTST";
        Version.versionCode = 100;
        Version.description = new String[]{
                "测试",
                "出货第版",
                "测控件透明度设置",
                "测双光融合"
        };

        Debugger.isDebug = true;//---------
//        LogManager.isDebug = true;//-------

        if (!Debugger.isDebug && !LogManager.isDebug)
            Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        System.exit(1);
    }
}
