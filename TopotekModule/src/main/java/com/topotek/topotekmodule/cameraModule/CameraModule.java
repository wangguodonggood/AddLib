package com.topotek.topotekmodule.cameraModule;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.view.SurfaceHolder;

import com.topotek.module.android.debug.Debugger;
import com.topotek.module.android.thread.MainThreadHandler;
import com.topotek.module.java.file.SimpleWriteFileUtils;
import com.topotek.module.java.threadExecutor.FunnelExecutor;
import com.topotek.module.java.threadExecutor.ThreadPool;
import com.topotek.module.language.objectSwitch.ObjectStreamSwitch;
import com.topotek.modules.modules0.project.parameterStorageModule.ParameterStorageModule;
import com.topotek.topotekmodule.SimplifyCallback.MyCameraManagerCaptureCallback;
import com.topotek.topotekmodule.UsbCameraRecordHelper;
import com.topotek.topotekmodule.cameraManage.AutoMaxFileRunnable;
import com.topotek.topotekmodule.cameraManage.CameraManager;
import com.topotek.topotekmodule.cameraManage.MaxFileRunnable;
import com.topotek.topotekmodule.cameraView.AutoPreviewView;
import com.topotek.topotekmodule.uiModule.UiModule;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
public class CameraModule {
    //创建一个Camera对象 android5.0之后过时
    public static Camera camera;
    public static SurfaceHolder surfaceHolder;
    private static byte[] usbCameraFrameData;
    private static Bitmap bitmap;
    public static MyCameraManagerCaptureCallback myCameraManagerCaptureCallback;
    public static CameraManager cameraManager;
    public static int initCamera(){
        try {
            //打开相机
            camera = Camera.open(0);
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }
        if(camera == null)
            return -2;
        //设置参数
        //Camera.getParameters()接口可以获取当前相机设备的默认配置参数
        Camera.Parameters parameters = camera.getParameters();
        if (parameters == null)
            return -3;
        //获取之前设置的白平衡的参数
        int cameraWhiteBalanceIndex =
                ParameterStorageModule.parameterStorage.getInt("cameraWhiteBalanceIndex", 0);

        //设置白平衡
        List<String> supportedWhiteBalanceValues = parameters.getSupportedWhiteBalance();
        String whiteBalanceValue = supportedWhiteBalanceValues.get(cameraWhiteBalanceIndex);
        parameters.setWhiteBalance(whiteBalanceValue);

        //设置曝光参数
        int cameraExposureCompensation = ParameterStorageModule
                .parameterStorage.getInt("cameraExposureCompensationValue", 0);
        parameters.setExposureCompensation(cameraExposureCompensation);

        //设设置感光度
        int cameraIsoIndex = ParameterStorageModule.parameterStorage.getInt("cameraIsoIndex", 0);
        String supportedIso = parameters.get("iso-speed-values");
        String[] supportedIsoValues = supportedIso.split(",");
        List<String> supportedIsoValues_List = Arrays.asList(supportedIsoValues);
        String supportedIsoValue = supportedIsoValues_List.get(cameraIsoIndex);
        parameters.set("iso-speed", supportedIsoValue);
        //设置预览分辨率
        parameters.setPreviewSize(1280, 720);
        //添加参数
        camera.setParameters(parameters);
        return 1;
    }
    //设置相机预览
    public static int previewCamera(Camera camera, SurfaceTexture surfaceTexture){
        try {
            camera.setPreviewTexture(surfaceTexture);
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
        try {
            camera.startPreview();
        }catch (Exception e){
            e.printStackTrace();
            return -2;
        }
        return 1;
    }

    public static void manageCamera(){
        myCameraManagerCaptureCallback =
                new MyCameraManagerCaptureCallback
                        (UiModule.dynamicDoublePreviewLayout.getContext(),
                                UiModule.cameraView0);
        cameraManager = new CameraManager(CameraModule.camera, 0,
                UiModule.previewView0.getSurfaceTexture(), myCameraManagerCaptureCallback);
    }

    public static int initUsbCamera(String usbCameraDeviceFilePath){
        return jniInitUsbCamera(usbCameraDeviceFilePath);
    }

    public static int usbCameraStreamOn(int buffersCount){

        int width = 640;
        int height = 480;
        usbCameraFrameData = new byte[width * height * 4];//width * height * argb

        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        return jniUsbCameraStreamOn(buffersCount, usbCameraFrameData);
    }

    private static native int jniInitUsbCamera(String usbCameraDeviceFilePath);

    private static native int jniUsbCameraStreamOn(int buffersCount, byte[] frameData);

    private static boolean isUsbCameraCapture = false;
    private static boolean isCaptureing = false;
    private static boolean isRecord = false;///!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!=====
    private static boolean isRecordDoing = false;

    public static void usbCameraCapture(){
        if(!isCaptureing && myCameraManagerCaptureCallback.checkTfCard(1))
            isUsbCameraCapture = true;
    }

    private static MaxFileRunnable maxFileRunnable;

    public static void usbCameraRecord(){
        if(!isRecordDoing && myCameraManagerCaptureCallback.checkTfCard(1)){
            isRecordDoing = true;
            if(isRecord){
                maxFileRunnable.isStop = true;
                isRecord = false;
                MainThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        UiModule.cameraView1.recordFinishUI();
                    }
                });
            } else {
                File outputFile = CameraModule.myCameraManagerCaptureCallback.getOutputFile(1, ".mp4");
                maxFileRunnable = new AutoMaxFileRunnable(outputFile, new AutoMaxFileRunnable.RecordCallback() {
                    @Override
                    public void onRecord() {
//                        usbCameraRecord();
                        UiModule.uiLayout.getContext().sendBroadcast(new Intent("com.topotek.service.data").putExtra("string", "#TPDD2wRECNARR"));
                    }
                });
                ThreadPool.schedule(maxFileRunnable, 20, TimeUnit.SECONDS);
                UsbCameraRecordHelper.initRecord(outputFile.getAbsolutePath(), 640, 480);
                isRecord = true;
                MainThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        UiModule.cameraView1.recordStartUI();
                    }
                });
                FunnelExecutor.schedule(new Runnable() {
                    @Override
                    public void run() {
                        isStop = true;
                        isRecordDoing = false;
                    }
                }, 1, TimeUnit.SECONDS);
            }
        }

//        if(!isRecordDoing && myCameraManagerCaptureCallback.checkTfCard(1)){
//            isRecordDoing = true;
//            if(mObjectStreamSwitch.getIsOnOrOff())
//                mObjectStreamSwitch.switchOff();
//            else {
//                mObjectStreamSwitch.switchOn();
//            }
//        }
    }

    private static boolean isStop = false;

    private static long time = 0;
    private static int fnum = 0;

    private static ObjectStreamSwitch<byte[]> mObjectStreamSwitch = new ObjectStreamSwitch<byte[]>(false, false) {
        @Override
        protected void on() {
            File outputFile = myCameraManagerCaptureCallback.getOutputFile(1, ".avi");
            jniVideoWriterInit(outputFile.getAbsolutePath());
            MainThreadHandler.post(new Runnable() {
                @Override
                public void run() {
                    UiModule.cameraView1.recordStartUI();
                }
            });
            FunnelExecutor.schedule(new Runnable() {
                @Override
                public void run() {
                    isRecordDoing = false;
                }
            }, 1, TimeUnit.SECONDS);
        }

        @Override
        protected void off() {
            jniVideoWriterUninit();
            MainThreadHandler.post(new Runnable() {
                @Override
                public void run() {
                    UiModule.cameraView1.recordFinishUI();
                }
            });
            isRecordDoing = false;
        }

        @Override
        protected void receiveObject(byte[] bytes) {
//            if(time == 0)
//                time = SystemClock.elapsedRealtime();
//            else {
//                ++fnum;
//                Debugger.show(fnum * 1000 / (SystemClock.elapsedRealtime() - time) + "  fps");
//            }
//            long l = SystemClock.elapsedRealtime();
            jniVideoWriter(bytes, bytes.length);
//            long l1 = SystemClock.elapsedRealtime();
//            Debugger.show((l1 - l) + "time2");//============================------------------===
        }
    };

    public static boolean onFrame(int length){

//        if(time == 0)
//            time = SystemClock.elapsedRealtime();
//        else {
//            ++fnum;
//            Debugger.show(fnum * 1000 / (SystemClock.elapsedRealtime() - time) + "  /");
//        }

        bitmap = BitmapFactory.decodeByteArray(usbCameraFrameData, 0, length);
//        bitmap.copyPixelsFromBuffer(ByteBuffer.wrap(usbCameraFrameData));

        if(bitmap == null){
            Debugger.addShow("    decodeErr+1");
            return false;
        } else{

            if(isRecord){
                ByteBuffer byteBuffer = ByteBuffer.allocate(bitmap.getByteCount());
                bitmap.copyPixelsToBuffer(byteBuffer);
                byte[] data = byteBuffer.array();
//                byte[] data = Arrays.copyOf(usbCameraFrameData, usbCameraFrameData.length);
                UsbCameraRecordHelper.putFrameData(data);
            }else {

                if(isStop){
                    isStop = false;
                    UsbCameraRecordHelper.stopRecord(new UsbCameraRecordHelper.RecordStopListener() {
                        @Override
                        public void onRecordStop() {
                            Debugger.addShow("    onRecordStop");
                            isRecordDoing = false;
                        }
                    });
                }
            }

            AutoPreviewView.drawBitmap(surfaceHolder, bitmap);

            if(isUsbCameraCapture){
                isCaptureing = true;
                isUsbCameraCapture = false;

                final byte[] bytes = Arrays.copyOf(usbCameraFrameData, length);

                ThreadPool.execute(new Runnable() {
                    @Override
                    public void run() {

                        MainThreadHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                UiModule.cameraView1.captureStartUI();
                            }
                        });
                        File outputFile = myCameraManagerCaptureCallback.getOutputFile(1, ".jpg");
                        SimpleWriteFileUtils.writeFile(outputFile, bytes);
                        MainThreadHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                UiModule.cameraView1.captureFinishUI();
                                isCaptureing = false;
                            }
                        }, 512);
                    }
                });
            }

//            if(runnableNummber < 5){
//                final byte[] bytes = Arrays.copyOf(usbCameraFrameData, length);
//                ++runnableNummber;
//                SynchronizedExecutor.execute(new Runnable() {
//                    @Override
//                    public void run() {
//                        mObjectStreamSwitch.sendObject(bytes);
//                        --runnableNummber;
//                    }
//                });
//            }

            return true;
        }
    }

    private static int runnableNummber = 0;

    private static boolean isVideoWriteInit = false;
    public static native int jniVideoWriterInit(String videoFilePath);
    public static native int jniVideoWriterUninit();
    public static native int jniVideoWriter(byte[] data, int length);
}
