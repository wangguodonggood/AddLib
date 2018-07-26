package com.topotek.modules.modules0.project.externalCommunicationModule;

import android.content.Context;

import com.topotek.libs.libs1.project.broadcastCommunication.BroadcastCommunicationModule;

/**
 * 调用BroadcastCommunicationModule
 * 1、使用接口BroadcastCommunicationDataListener传递数据过来
 * 2、进一步封装了 向串口发送数据的方法
 */
public class ExternalCommunicationModule {

    private static Context context;

    public static void init(Context context, final ExternalCommunicationDataListener
            externalCommunicationDataListener) {

        //??? 为什么定义一个静态的context
        ExternalCommunicationModule.context = context;

        BroadcastCommunicationModule.init(context,
                new BroadcastCommunicationModule.BroadcastCommunicationDataListener() {
            @Override
            public void onData(String data) {
                //将数据给了externalCommunicationDataListener接口
                externalCommunicationDataListener.onData(data);
            }
        });
    }

    public static void sendData(String data) {
        BroadcastCommunicationModule.sendData(context, data);
    }

    public static void sendCommand(String command) {
        BroadcastCommunicationModule.sendCommand(context, command);
    }

    public static void sendXYWH(int x, int y, int width, int height) {
        BroadcastCommunicationModule.sendXYWH(context, x, y, width, height);
    }

    public interface ExternalCommunicationDataListener {
        void onData(String data);
    }
}
