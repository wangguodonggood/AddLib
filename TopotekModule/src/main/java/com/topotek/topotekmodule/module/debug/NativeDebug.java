package com.topotek.topotekmodule.module.debug;

import com.topotek.module.android.debug.Debugger;

public class NativeDebug {

    public static void show(String text){
        Debugger.show(text);
    }

    public static void addShow(String text){
        Debugger.addShow(text);
    }
}
