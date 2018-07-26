package com.topotek.libs.libs1.project.broadcastCommunication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class CommunicationDataReceiver extends BroadcastReceiver {//ok

    private BroadcastCommunicationModule.BroadcastCommunicationDataListener
            mBroadcastCommunicationDataListener;

    //new对象的时候传进来一个接口对象
    //在接收到onReceive广播之后，就有了数据
    CommunicationDataReceiver(
            BroadcastCommunicationModule.BroadcastCommunicationDataListener
                    broadcastCommunicationDataListener) {
        mBroadcastCommunicationDataListener = broadcastCommunicationDataListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String data = intent.getStringExtra("string");
        mBroadcastCommunicationDataListener.onData(data);
    }
}
