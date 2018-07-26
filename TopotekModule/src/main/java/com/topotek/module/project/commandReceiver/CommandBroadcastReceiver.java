package com.topotek.module.project.commandReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class CommandBroadcastReceiver extends BroadcastReceiver {

    private CommandReceiver.CommandListener commandListener;

    public CommandBroadcastReceiver(CommandReceiver.CommandListener commandListener){
        this.commandListener = commandListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String command = intent.getStringExtra("string");
        commandListener.onCommand(command);
    }
}
