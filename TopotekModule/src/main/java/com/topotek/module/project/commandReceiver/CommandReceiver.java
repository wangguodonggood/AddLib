package com.topotek.module.project.commandReceiver;

import android.content.Context;
import android.content.IntentFilter;

public class CommandReceiver {

    public static void startReceive(Context context, final CommandListener commandListener) {

        CommandBroadcastReceiver commandBroadcastReceiver = new CommandBroadcastReceiver(commandListener);
        IntentFilter intentFilter = new IntentFilter("com.topotek.service.data");
        context.registerReceiver(commandBroadcastReceiver, intentFilter);
    }

    public interface CommandListener {
        void onCommand(String command);
    }
}
