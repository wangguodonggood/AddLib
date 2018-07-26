package com.topotek.topotekmodule.cameraManage;

import android.content.Context;
import android.content.Intent;

import com.topotek.module.java.threadExecutor.FunnelExecutor;
import com.topotek.module.java.threadExecutor.ThreadPool;
import com.topotek.topotekmodule.uiModule.UiModule;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class AutoMaxFileRunnable extends MaxFileRunnable {

    private File file;
    private RecordCallback recordCallback;

    public AutoMaxFileRunnable(File f, RecordCallback r){
        file = f;
        recordCallback = r;
    }

    @Override
    public void run() {
        if(isStop)
            return;
        boolean exists = file.exists();
        if(exists){
            long length = file.length();//max = 4G * 1024m * 1024kb * 1024b = 4294967296b;
            if(length > 4190109696L){//100m * 1024kb * 1024b = 104857600b;  4294967296b - 104857600b = 4190109696b;
                recordCallback.onRecord();
                ThreadPool.schedule(new Runnable() {
                    @Override
                    public void run() {
                        recordCallback.onRecord();
                    }
                }, 5, TimeUnit.SECONDS);
            }else
                ThreadPool.schedule(this, 20, TimeUnit.SECONDS);
        }
    }

    public interface RecordCallback{

        void onRecord();
    }
}
