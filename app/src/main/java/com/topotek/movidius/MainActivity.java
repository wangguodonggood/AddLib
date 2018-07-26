package com.topotek.movidius;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.topotek.module.android.debug.Debugger;
import com.topotek.module.android.debug.LogManager;
import com.topotek.module.android.thread.MainThreadHandler;
import com.topotek.module.project.uiView.UiLayout;
import com.topotek.movidius.jni.JNI;
import com.topotek.topotekmodule.ProjectModule;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity
        implements  ProjectModule.CommandListener,
        View.OnClickListener,ProjectModule.StartPreviewListener,ProjectModule.PreviewFrameCallback{
    private static String TAG=MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //初始化调试模式
        Debugger.init(this,this);
        super.onCreate(savedInstanceState);
        //隐藏虚拟按键(导航栏)
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        //如果需要确定视频预览之后 才能完成需要添加监听StartPreviewListener
        UiLayout uiLayout=ProjectModule.initUiView(this,this);
        uiLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //如果点击外层布局就发送菜单键命令
                sendBroadcast(new Intent("com.topotek.service.data")
                        .putExtra("string", ":wKey6"));
            }
        });
        setContentView(uiLayout);
        //初始化命令监听 可以接收和发送命令
        ProjectModule.initCommandListener(this, this);
        ProjectModule.setOnPreviewFrameCallback(this);
        new Thread(){
            @Override
            public void run() {
               MainThreadHandler.post(new Runnable() {
                   @Override
                   public void run() {

                   }
               });
            }
        }.start();
    }
    //作为程序的入口
    public native int intFromJNI();
    static {
        System.loadLibrary("native-lib");
    }
    @Override
    public void onCommand(String command) {
        switch (command){
            //开始调试
            case ":wKey8":
                Debugger.isDebug = true;//---------
                LogManager.isDebug = true;//-------
                MainThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Debugger.init(MainActivity.this, MainActivity.this);
                    }
                });
                break;
        }
    }
    @Override
    public void onClick(View v) {
    }
    @Override
    public void onStartPreview(int cameraFacing) {
        //预览成功回调
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                getContentResolver().getType(Uri.parse("content://com.topotek.service"));
            }
        }, 5000, 5000);
        //获取一帧数据
        //ProjectModule.getOneFrame(true);
        //发送命令给C语言
        JNI.onCommand("");
    }
    @Override
    public void onPreviewFrame(int cameraFacing, byte[] frameData, int frameWidth, int frameHeight) {
        //获取一帧数据成功后回调
        JNI.onPreviewFrame(frameData,frameWidth,frameHeight);
    }
}

