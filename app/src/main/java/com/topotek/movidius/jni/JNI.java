package com.topotek.movidius.jni;

import android.util.Log;

import com.topotek.topotekmodule.ProjectModule;
/**
 * Created by wgd on 2018/5/18.
 * C 方法
 */
public class JNI {

    //java去调用C++

    public static native void onPreviewFrame(byte[] frameData, int frameWidth, int frameHeight);

    public static native void onCommand(String command);

    //供给C++语言调用java
    public static void getOneFrame(){
        ProjectModule.getOneFrame(false);
    }
    public static void resultToJava(int result){
        Log.i("LogWgd", "getResult: "+result);
    }

}
