package com.topotek.libs.libs1.project.broadcastCommunication;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;

import com.topotek.module.java.threadExecutor.FunnelExecutor;

import java.util.concurrent.TimeUnit;
/**
 * 初始化之后就可以调用这里面的方法 发送数据给串口服务程序 并获取串口传递过来的数据
 */
public class BroadcastCommunicationModule {
    //负责接收数据的接口
    public interface BroadcastCommunicationDataListener {
        void onData(String data);
    }

    public static void init(Context context, BroadcastCommunicationDataListener
            broadcastCommunicationDataListener) {

        //动态注册广播  com.topotek.service.data 接收串口传递过来的数据给了接口
        context.registerReceiver(new CommunicationDataReceiver(broadcastCommunicationDataListener),
                new IntentFilter("com.topotek.service.data"));

        //在相机中唤醒串口服务程序
        final ContentResolver contentResolver = context.getContentResolver();
        final Uri uri = Uri.parse("content://com.topotek.service");
        FunnelExecutor.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                contentResolver.getType(uri);//唤醒串口服务程序
            }
        }, 2, 6, TimeUnit.SECONDS);
    }

    //*********************以下方法是发送数据给串口服务*************************************
    public static void sendData(Context context, String data) {
        context.sendBroadcast(new Intent("com.topotek.service.write")
                .putExtra("string", data));
    }
    public static void sendCommand(Context context, String command) {
        //发送数据给串口服务程序
        context.sendBroadcast(new Intent("com.topotek.service.write")
                .putExtra("command", command));
    }

    public static void sendXYWH(Context context, int x, int y, int width, int height) {
        //发送数据给串口服务程序
        Intent intent = new Intent("com.topotek.service.xy");
        intent.putExtra("x", x).putExtra("y", y)
                .putExtra("w", width).putExtra("h", height);
        context.sendBroadcast(intent);
    }


}
