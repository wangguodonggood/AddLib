package com.topotek.topotekmodule;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.SurfaceHolder;

import com.topotek.libs.libs0.project.language.Strings;
import com.topotek.libs.libs1.project.parameterStorage.ParameterStorage;
import com.topotek.module.android.debug.Debugger;
import com.topotek.module.android.thread.MainThreadHandler;
import com.topotek.module.java.threadExecutor.ThreadPool;
import com.topotek.module.project.commandAnalyzer.CommandAnalyzer;
import com.topotek.module.project.uiView.UiLayout;
import com.topotek.modules.modules0.project.externalCommunicationModule.ExternalCommunicationModule;
import com.topotek.modules.modules0.project.parameterStorageModule.ParameterStorageModule;
import com.topotek.topotekmodule.SimplifyCallback.MySurfaceHolderCallback;
import com.topotek.topotekmodule.cameraModule.CameraDetector;
import com.topotek.topotekmodule.cameraModule.CameraModule;
import com.topotek.topotekmodule.commandExecuter.CommandExecuter;
import com.topotek.topotekmodule.task.CameraInitTask;
import com.topotek.topotekmodule.task.TfCardCheckTask;
import com.topotek.topotekmodule.uiModule.UiModule;

import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;
//TopotekModule项目封装好的入口
public class ProjectModule {
    private static PreviewFrameCallback previewFrameCallback;
    public static UiLayout initUiView(Context context, StartPreviewListener startPreviewListener){
        //管理default里面的参数
        ParameterStorageModule.parameterStorage
                = new ParameterStorage(context, "default");//------------
       //取出之前设置的语言
        Strings.language =
                ParameterStorageModule.parameterStorage.getInt("language", Strings.Language_Chinses);
        //初始化一个动态的双预览摄像头 并初始化了Meau菜单布局
        UiLayout uiView = UiModule.initUiView(context);
        //检测TF卡的状态
        TfCardCheckTask.exec();
        //增加主摄像头的view显示
        UiModule.addCameraView0(UiModule.dynamicDoublePreviewLayout.getContext(),
                //传进去一个重写的newSurfaceTextureListener
                CameraInitTask.newSurfaceTextureListener(startPreviewListener));
        //检测Usb摄像头
        CameraDetector.detectUsbCamera(newUsbCameraDetectCallback(context));
        //返回给Activity显示的界面
        return uiView;
    }
    //检测到Usb摄像头回掉的监听
    private static CameraDetector.UsbCameraDetectCallback newUsbCameraDetectCallback(final Context context){
        return new CameraDetector.UsbCameraDetectCallback() {
            @Override
            public void onDetect(final String usbCameraDeviceFilePath) {
                ThreadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        Debugger.addShow("\n" + usbCameraDeviceFilePath);
                        //加载jni库
                        System.loadLibrary("jni");
                        int i = CameraModule.initUsbCamera(usbCameraDeviceFilePath);
                        if(i < 0) {
                            Debugger.addShow("    initUsbCamErr" + i);
                            return;
                        }
                        MainThreadHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                UiModule.addCameraView1(UiModule.dynamicDoublePreviewLayout.getContext(),
                                        newSurfaceHolderCallback());
                            }
                        });
                    }
                });
            }
        };
    }
    private static SurfaceHolder.Callback newSurfaceHolderCallback(){
        return new MySurfaceHolderCallback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                CameraModule.surfaceHolder = surfaceHolder;
                ThreadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        int i = CameraModule.usbCameraStreamOn(1);
                        if(i < 0)
                            Debugger.addShow("    usbCameraErr" + i);
                    }
                });
            }
        };
    }
    private static Runnable getOneFrameRunnable = new Runnable() {
        @Override
        public void run() {
            Bitmap bitmap = UiModule.previewView0.getBitmap(160, 90);//160, 90
            if(bitmap == null){
                ThreadPool.schedule(this, 50, TimeUnit.MILLISECONDS);
                return;
            }
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int byteCount = bitmap.getByteCount();
            if(width < 1 || height < 1 || byteCount < 1){
                ThreadPool.schedule(this, 50, TimeUnit.MILLISECONDS);
                return;
            }
            ByteBuffer byteBuffer = ByteBuffer.allocate(byteCount);
            bitmap.copyPixelsToBuffer(byteBuffer);
            byte[] array = byteBuffer.array();
            if(array.length < 1){
                ThreadPool.schedule(this, 50, TimeUnit.MILLISECONDS);
                return;
            }
            previewFrameCallback.onPreviewFrame(0, array, width, height);
        }
    };
    public static void getOneFrame(boolean isDelay){
        if(isDelay)
            ThreadPool.schedule(getOneFrameRunnable, 50, TimeUnit.MILLISECONDS);
        else
            ThreadPool.execute(getOneFrameRunnable);
    }
    public static void initCommandListener(Context context, final CommandListener commandListener){
        //命令执行
        final CommandExecuter commandExecuter =
                new CommandExecuter(context, UiModule.dynamicDoublePreviewLayout);
        //从ExternalCommunicationModule接收命令 并执行
        ExternalCommunicationModule.init(context,
                new ExternalCommunicationModule.ExternalCommunicationDataListener() {
            @Override
            public void onData(final String data) {
                Debugger.show("    cmd@" + data);
                ThreadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        //把命令交给commandExecuter这个实现类 去执行命令
                        CommandAnalyzer.analyzeCommand(data, commandExecuter);
                        if(commandListener != null)
                            commandListener.onCommand(data);
                    }
                });
            }
        });
    }
    public static void setOnPreviewFrameCallback(PreviewFrameCallback onPreviewFrameCallback){
        previewFrameCallback = onPreviewFrameCallback;
    }

    public interface CommandListener {
        void onCommand(String command);
    }
    public interface StartPreviewListener {
        void onStartPreview(int cameraFacing);
    }
    public interface PreviewFrameCallback {
        void onPreviewFrame(int cameraFacing, byte[] frameData, int frameWidth, int frameHeight);
    }
}
