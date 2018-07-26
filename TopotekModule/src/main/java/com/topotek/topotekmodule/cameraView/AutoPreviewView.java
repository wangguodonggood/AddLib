package com.topotek.topotekmodule.cameraView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.topotek.module.android.debug.Debugger;
import com.topotek.module.android.thread.MainThreadHandler;
import com.topotek.module.java.threadExecutor.FunnelExecutor;
import com.topotek.module.java.threadExecutor.ThreadPool;
import com.topotek.topotekmodule.SimplifyCallback.MySurfaceHolderCallback;
import com.topotek.topotekmodule.cameraModule.CameraModule;
import com.topotek.topotekmodule.uiModule.UiModule;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class AutoPreviewView extends SurfaceView {

    private SurfaceHolder mSurfaceHolder;

    public AutoPreviewView(Context context) {
        super(context);

        getHolder().addCallback(createSurfaceHolderCallback(this));
    }

    public static SurfaceHolder.Callback createSurfaceHolderCallback(final AutoPreviewView autoPreviewView) {
        return new MySurfaceHolderCallback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {

                autoPreviewView.mSurfaceHolder = surfaceHolder;

                ThreadPool.execute(createRunnable(autoPreviewView));
            }
        };
    }

    private static Runnable createRunnable(final AutoPreviewView autoPreviewView) {
        return new Runnable() {
            @Override
            public void run() {

                System.loadLibrary("jni");

                int width = 640;
                int height = 480;
                autoPreviewView.frameData = new byte[720 * 576 * 4];

                autoPreviewView.mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

                for(int i = 5; i >= 0; ) {

                    final String path = detectUsbCameraExist(i);
                    if(path != null){

                        Debugger.addShow("\n" + path);

                        isToUsbCameraC = true;
                        final int log = autoPreviewView.usbCamera(path, width, height, 4, autoPreviewView.frameData);

                        Debugger.addShow("    usbCamera " + log);

                        i++;
                    }

                    try {
                        Thread.sleep(2048);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }

    public native int usbCamera(String usbCameraDeviceFilePath, int width, int height, int buffersCount, byte[] buffer);

    private byte[] frameData;
    private Bitmap mBitmap;

    private static boolean isCapture = false;

    public boolean onFrame(){

        mBitmap = BitmapFactory.decodeByteArray(frameData, 0, frameData.length);

//        mBitmap.copyPixelsFromBuffer(ByteBuffer.wrap(frameData));

        if(mBitmap == null){
            Debugger.addShow("    decodeErr+1");
            return false;
        } else{
            cap();
            drawBitmap(mSurfaceHolder, mBitmap);
            return true;
        }
    }

    public static void drawBitmap(SurfaceHolder surfaceHolder, Bitmap bitmap) {

        if(!isShowImage)
            return;

        Canvas canvas = surfaceHolder.lockCanvas();

        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();

        if (isRorate)
            canvas.rotate(180, canvasWidth / 2, canvasHeight / 2);//图像翻转

        canvas.drawBitmap(bitmap, null, new Rect(0, 0, canvasWidth, canvasHeight), null);

        surfaceHolder.unlockCanvasAndPost(canvas);

//        Debugger.addShow(Debugger.getActivity(), "S");
    }

    public static boolean isShowImage = true;

    public static boolean isRorate = false;

    public static native void record();

    private static boolean isToUsbCameraC = false;

    private static boolean isUsbCameraCaptureUnLock = true;

    public static void usbCameraCapture(){
        if(isUsbCameraCaptureUnLock && CameraModule.myCameraManagerCaptureCallback.checkTfCard(1)){
            isUsbCameraCaptureUnLock = false;
            isCapture = !isCapture;
            FunnelExecutor.schedule(new Runnable() {
                @Override
                public void run() {
                    isUsbCameraCaptureUnLock = true;
                }
            }, 1, TimeUnit.SECONDS);
        }
    }

    public static void usbCameraRecord(){

        if(isToUsbCameraC){
            isToUsbCameraC = false;
            boolean isHaveTfCard = CameraModule.myCameraManagerCaptureCallback.checkTfCard(1);
            if(isHaveTfCard){
                record();
                FunnelExecutor.schedule(new Runnable() {
                    @Override
                    public void run() {
                        isToUsbCameraC = true;
                    }
                }, 1, TimeUnit.SECONDS);
                MainThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        UiModule.cameraView1.recordUI();
                    }
                });
            }
        }
    }

    public String getOutputFileName(){
        File outputFile = CameraModule.myCameraManagerCaptureCallback.getOutputFile(1, ".avi");
        return outputFile.getAbsolutePath();
    }

    private void cap(){
        if(isCapture){
            File outputFile = CameraModule.myCameraManagerCaptureCallback.getOutputFile(1, ".jpg");
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(outputFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                isCapture = false;
            }
            if(isCapture){
                MainThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        UiModule.cameraView1.captureStartUI();
                    }
                });
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
                try {
                    bufferedOutputStream.write(frameData);
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    try {
                        bufferedOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    isCapture = false;
                    MainThreadHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            UiModule.cameraView1.captureFinishUI();
                        }
                    }, 512);
                }
            }
        }
    }

    public static String detectUsbCameraExist(int max){
        for(int i = 0; i <= max; ++i){
            String s = "dev/video" + i;
            File usbCameraDevicefile = new File(s);
            if(usbCameraDevicefile.exists())
                return s;
        }
        return null;
    }
}
