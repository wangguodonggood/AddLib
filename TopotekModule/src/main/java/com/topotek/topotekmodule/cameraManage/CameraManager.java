package com.topotek.topotekmodule.cameraManage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.util.Size;
import android.view.Surface;

import com.topotek.module.android.bitMap.BitMapUtils;
import com.topotek.module.android.thread.MainThreadHandler;
import com.topotek.module.java.threadExecutor.ThreadPool;
import com.topotek.modules.modules0.project.parameterStorageModule.ParameterStorageModule;
import com.topotek.topotekmodule.uiModule.UiModule;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class CameraManager implements Camera.PictureCallback {

    private int cameraFacing;

    private Surface mSurface;

    private Camera mCamera;

    private int state = 0;
    public static final int STATE_PREVIEWING = 1;//预览中
    public static final int STATE_DOING = 2;//操作中
    private static final int STATE_RECORDING = 3;//录像中

    private Size[] pictureSizes;
    private int pictureSizeIndex;
    private Size[] videoSizes;
    private int videoSizeIndex;

    private MediaRecorder mMediaRecorder;

    private CaptureCallback mCaptureCallback;

    public void setPreviewCallback(Camera.PreviewCallback previewCallback){
        mCamera.setPreviewCallback(previewCallback);
    }

    public CameraManager(Camera camera, int facing, SurfaceTexture surfaceTexture, CaptureCallback captureCallback) {
        mCamera = camera;
        cameraFacing = facing;
        mSurface = new Surface(surfaceTexture);
        mCaptureCallback = captureCallback;
        init();
    }

    public int getState() {
        return state;
    }

    public Camera.Size getPreviewSize() {
        Camera.Parameters parameters = mCamera.getParameters();
        return parameters.getPreviewSize();
    }

    public byte[] createCallbackBuffer() {

        Camera.Parameters parameters = mCamera.getParameters();
        Camera.Size previewSize = parameters.getPreviewSize();
        int previewFormat = parameters.getPreviewFormat();
        int bitsPerPixel = ImageFormat.getBitsPerPixel(previewFormat);
        int size = previewSize.width * previewSize.height * bitsPerPixel / 8;
        return new byte[size];
    }

    public void addCallbackBuffer(byte[] bytes) {
        mCamera.addCallbackBuffer(bytes);
    }

    public void setPreviewCallbackWithBuffer(Camera.PreviewCallback previewCallback) {

        mCamera.setPreviewCallbackWithBuffer(previewCallback);
    }

    private void init() {

        pictureSizes = new Size[4];
        pictureSizes[0] = new Size(2688, 1520);//400w    ≈16:9
        pictureSizes[1] = new Size(3264, 2448);//800w    4:3
        pictureSizes[2] = new Size(4160, 3120);//1300w    4:3
        pictureSizes[3] = new Size(4618, 3464);//1600w    ≈4:3
        pictureSizeIndex = ParameterStorageModule.parameterStorage.getInt("cameraPictureSizeIndex", 3);//==============================

        videoSizes = new Size[2];
        videoSizes[0] = new Size(1280, 720);
        videoSizes[1] = new Size(1920, 1080);
        videoSizeIndex = ParameterStorageModule.parameterStorage.getInt("cameraVideoSizeIndex", 1);//====================================

        int pictureWidth = pictureSizes[pictureSizeIndex].getWidth();
        int pictureHeight = pictureSizes[pictureSizeIndex].getHeight();
        int i = CameraParametersUtils.setPictureSize(mCamera, pictureWidth, pictureHeight);//  4592  3072       4624  3472
        if (i == -1)
            pictureSizeIndex = -1;

        state = STATE_PREVIEWING;
    }

    public int getPictureSizeIndex() {
        return pictureSizeIndex;
    }

    public int getVideoSizeIndex() {
        return videoSizeIndex;
    }

    /**
     * @return 若会出现空指针则返回null;
     * 若不是预览状态则返回null;
     */
    public Size setPictureSize(int index) {

        if (state != STATE_PREVIEWING)
            return null;

        index = remain(index, pictureSizes.length);

        ParameterStorageModule.parameterStorage.putInt("cameraPictureSizeIndex", index);//==============================================

        int pictureWidth = pictureSizes[index].getWidth();
        int pictureHeight = pictureSizes[index].getHeight();
        int i = CameraParametersUtils.setPictureSize(mCamera, pictureWidth, pictureHeight);
        if (i == -1) {
            return null;
        }

        pictureSizeIndex = index;

        return pictureSizes[pictureSizeIndex];
    }

    /**
     * @return 若会出现空指针则返回null;
     * 若不是预览状态则返回null;
     */
    public Size changePictureSize(int change) {
        return setPictureSize(pictureSizeIndex + change);
    }

    /**
     * @return 若不是预览状态则返回null;
     */
    public Size setVideoSize(int index) {

        if (state != STATE_PREVIEWING)
            return null;

        videoSizeIndex = remain(index, videoSizes.length);

        ParameterStorageModule.parameterStorage.putInt("cameraVideoSizeIndex", index);//=================================================

        return videoSizes[videoSizeIndex];
    }
    /**
     * @return 若不是预览状态则返回null;
     */
    public Size changeVideoSize(int change) {
        return setVideoSize(videoSizeIndex + change);
    }

    private boolean isSnapshoot = false;

    public void setSnapshoot(boolean isSnapshoot){
        this.isSnapshoot = isSnapshoot;
    }

    public boolean picture() {

        if (state == STATE_DOING || !mCaptureCallback.checkTfCard(cameraFacing))
            return false;

        int state = this.state;
        this.state = STATE_DOING;

        if(state == STATE_RECORDING || isSnapshoot){
            MainThreadHandler.post(new Runnable() {
                @Override
                public void run() {
                    mCaptureCallback.onPictureStart(cameraFacing);
                }
            });
            File outputFile = mCaptureCallback.getOutputFile(cameraFacing, ".jpg");
            Bitmap bitmap = UiModule.previewView0.getBitmap();
            if(bitmap != null)
                BitMapUtils.compressToFile(bitmap, outputFile, Bitmap.CompressFormat.JPEG, 100);
            MainThreadHandler.post(new Runnable() {
                @Override
                public void run() {
                    mCaptureCallback.onPictureFinish(cameraFacing);
                }
            });

            this.state = state;

            return true;
        }

        MainThreadHandler.post(new Runnable() {
            @Override
            public void run() {

                mCaptureCallback.onPictureStart(cameraFacing);

                mCamera.takePicture(null, null, CameraManager.this);
            }
        });

        return true;
    }

    @Override
    public void onPictureTaken(final byte[] data, final Camera camera) {

        ThreadPool.execute(new Runnable() {
            @Override
            public void run() {

                File outputFile = mCaptureCallback.getOutputFile(cameraFacing, ".jpg");
                CameraCaptureUtils.savePicture(data, outputFile);

                camera.startPreview();

                mCaptureCallback.onPictureFinish(cameraFacing);

                state = STATE_PREVIEWING;
            }
        });
    }

    public void record() {

        if (state == STATE_PREVIEWING && mCaptureCallback.checkTfCard(cameraFacing)){
            state = STATE_DOING;
            ThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    startRecord();
                }
            });
        } else if (state == STATE_RECORDING){
            state = STATE_DOING;
            ThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    stopRecord();
                }
            });
        }
    }

    private MaxFileRunnable maxFileRunnable;

    private void startRecord() {

        mCamera.unlock();

        mMediaRecorder = new MediaRecorder();
        mMediaRecorder.reset();
        mMediaRecorder.setCamera(mCamera);

        Size videoSize = videoSizes[videoSizeIndex];
        final File outputFile = mCaptureCallback.getOutputFile(cameraFacing, ".mp4");

        CameraCaptureUtils.setMediaRecorder(mMediaRecorder, videoSize, outputFile);

        mMediaRecorder.setPreviewDisplay(mSurface);

        try {
            mMediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mMediaRecorder.start();

        mCaptureCallback.onRecordStart(cameraFacing);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                state = STATE_RECORDING;
            }
        }, 1024);

        maxFileRunnable = new MaxFileRunnable() {
            @Override
            public void run() {
                if(isStop)
                    return;
                boolean exists = outputFile.exists();
                if(exists){
                    long length = outputFile.length();//max = 4G * 1024m * 1024kb * 1024b = 4294967296b;
                    if(length > 4190109696L){//100m * 1024kb * 1024b = 104857600b;  4294967296b - 104857600b = 4190109696b;
                        final Context context = UiModule.uiLayout.getContext();
//                        record();
                        UiModule.uiLayout.getContext().sendBroadcast(new Intent("com.topotek.service.data").putExtra("string", "#TPDD2wRECANRR"));
                        ThreadPool.schedule(new Runnable() {
                            @Override
                            public void run() {
//                                record();
                                UiModule.uiLayout.getContext().sendBroadcast(new Intent("com.topotek.service.data").putExtra("string", "#TPDD2wRECANRR"));
                            }
                        }, 5, TimeUnit.SECONDS);
                    }else
                        ThreadPool.schedule(this, 20, TimeUnit.SECONDS);
                }
            }
        };
        ThreadPool.schedule(maxFileRunnable, 20, TimeUnit.SECONDS);
    }

    private void stopRecord() {

        if(maxFileRunnable != null)
            maxFileRunnable.isStop = true;

        mMediaRecorder.stop();
        mMediaRecorder.reset();
        mMediaRecorder.release();

        mCamera.lock();
        try {
            mCamera.reconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        state = STATE_PREVIEWING;

        mCaptureCallback.onRecordFinish(cameraFacing);
    }

    /**
     * @return 若会出现空指针则返回-1;
     */
    public int getWhiteBalanceIndex() {
        return CameraParametersUtils.getWhiteBalanceIndex(mCamera);
    }

    /**
     * @return 若会出现空指针则返回Integer.MIN_VALUE;
     */
    public int getExposureCompensation() {
        int minExposureCompensation = mCamera.getParameters().getMinExposureCompensation();
        int exposureCompensation = CameraParametersUtils.getExposureCompensation(mCamera);
        return exposureCompensation - minExposureCompensation;
    }

    /**
     * @return 若会出现空指针则返回-1;
     */
    public int getIsoIndex() {
        return CameraParametersUtils.getIsoIndex(mCamera);
    }

    /**
     * @return 返回设置的白平衡;若不是预览状态则返回null;若会出现空指针则返回null;
     */
    public String setWhiteBalance(int index) {

        if (state != STATE_PREVIEWING)
            return null;

        return CameraParametersUtils.setWhiteBalance(mCamera, index);
    }

    /**
     * @return 返回设置的白平衡;若不是预览状态则返回null;若会出现空指针则返回null;
     */
    public String changeWhiteBalance(int change) {

        if (state != STATE_PREVIEWING)
            return null;

        return CameraParametersUtils.changeWhiteBalance(mCamera, change);
    }

    /**
     * @return 返回设置的曝光;
     * 若不是预览状态则返回Integer.MIN_VALUE;
     * 若会出现空指针则返回Integer.MIN_VALUE;
     */
    public int setExposureCompensation(int value) {

        if (state != STATE_PREVIEWING)
            return Integer.MIN_VALUE;

        int minExposureCompensation = mCamera.getParameters().getMinExposureCompensation();

        return CameraParametersUtils.setExposureCompensation(mCamera, value + minExposureCompensation);
    }

    /**
     * @return 返回设置的曝光;
     * 若不是预览状态则返回Integer.MIN_VALUE;
     * 若会出现空指针则返回Integer.MIN_VALUE;
     */
    public int changeExposureCompensation(int change) {

        if (state != STATE_PREVIEWING)
            return Integer.MIN_VALUE;

        return CameraParametersUtils.changeExposureCompensation(mCamera, change);
    }

    /**
     * @return 返回设置的感光度;若不是预览状态则返回null;若会出现空指针则返回null;
     */
    public String setISO(int index) {

        if (state != STATE_PREVIEWING)
            return null;

        return CameraParametersUtils.setIso(mCamera, index);
    }

    /**
     * @return 返回设置的感光度;若不是预览状态则返回null;若会出现空指针则返回null;
     */
    public String changeISO(int change) {

        if (state != STATE_PREVIEWING)
            return null;

        return CameraParametersUtils.changeIso(mCamera, change);
    }

    public void setDisplayInverseOrientation(boolean b){
        if(b)
            mCamera.setDisplayOrientation(180);
        else
            mCamera.setDisplayOrientation(0);
        CameraCaptureUtils.isInverseOrientation = b;
    }

    public void setGpsLatitude(double gpsLatitude){

        if (state != STATE_PREVIEWING)
            return;

        Camera.Parameters parameters = mCamera.getParameters();
        //纬度
        parameters.setGpsLatitude(gpsLatitude);
        mCamera.setParameters(parameters);
    }

    public void setGpsLongitude(double gpsLongitude){

        if (state != STATE_PREVIEWING)
            return;

        Camera.Parameters parameters = mCamera.getParameters();
        //经度
        parameters.setGpsLongitude(gpsLongitude);
        mCamera.setParameters(parameters);
    }

    /**
     * @param dividend 可为负数
     * @param divisor  必须是正数
     * @return 返回余数;若divisor不是正数则返回-1;
     */
    private static int remain(int dividend, int divisor) {

        if (divisor <= 0) {
            return -1;
        }

        if (dividend < 0) {
            dividend = divisor - (-dividend % divisor);
        }

        return dividend % divisor;
    }

    public interface CaptureCallback {

        void onPictureStart(int cameraFacing);

        void onPictureFinish(int cameraFacing);

        void onRecordStart(int cameraFacing);

        void onRecordFinish(int cameraFacing);

        boolean checkTfCard(int cameraFacing);

        File getOutputFile(int cameraFacing, String file_Extension);
    }
}
