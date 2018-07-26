package com.topotek.topotekmodule.cameraModule;

import android.hardware.Camera;

import com.topotek.module.java.threadExecutor.FunnelExecutor;

import java.io.File;
import java.util.concurrent.TimeUnit;
//这个类用来检测摄像头是否可用
public class CameraDetector {

    //检测内置摄像头
    public static void detectCamera(final CameraDetectCallback cameraDetectCallback){
        FunnelExecutor.execute(new Runnable() {
            @Override
            public void run() {
                int numberOfCameras = Camera.getNumberOfCameras();
                if(numberOfCameras > 0)
                    //如果摄像头数量是否大于0
                    cameraDetectCallback.onDetect();
                else {
                    cameraDetectCallback.noDetect();
                    FunnelExecutor.schedule(this, 1, TimeUnit.SECONDS);
                }
            }
        });
    }

    //检测USB摄像头是否检测到
    public static void detectUsbCamera(final UsbCameraDetectCallback usbCameraDetectCallback){
        FunnelExecutor.execute(new Runnable() {
            //如果有摄像头插入 那么文件就会存在
            private File usbCameraDevicefile = new File("dev/video0");
            @Override
            public void run() {
                //如果摄像头存在
                if(usbCameraDevicefile.exists())
                    //回掉onDetect（）  把文件的绝对路径传出去
                    usbCameraDetectCallback.onDetect(usbCameraDevicefile.getAbsolutePath());
                else
                    //每隔一秒就检测一次 循环检测
                    FunnelExecutor.schedule(this, 1, TimeUnit.SECONDS);
            }
        });
    }

    public interface CameraDetectCallback{

        void onDetect();

        void noDetect();
    }

    public interface UsbCameraDetectCallback{
        void onDetect(String usbCameraDeviceFilePath);
    }
}