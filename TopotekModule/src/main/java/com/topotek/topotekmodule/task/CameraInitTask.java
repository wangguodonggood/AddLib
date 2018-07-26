package com.topotek.topotekmodule.task;

import android.graphics.Color;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.view.TextureView;

import com.topotek.libs.libs0.project.language.Strings;
import com.topotek.module.android.thread.MainThreadHandler;
import com.topotek.module.java.threadExecutor.ThreadPool;
import com.topotek.topotekmodule.ProjectModule;
import com.topotek.topotekmodule.SimplifyCallback.MySurfaceTextureListener;
import com.topotek.topotekmodule.cameraModule.CameraDetector;
import com.topotek.topotekmodule.cameraModule.CameraModule;
import com.topotek.topotekmodule.uiModule.UiModule;

public class CameraInitTask{
    public static TextureView.SurfaceTextureListener newSurfaceTextureListener(
            final ProjectModule.StartPreviewListener startPreviewListener) {
         return new MySurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
                CameraDetector.detectCamera(newCameraDetectCallback(surfaceTexture, startPreviewListener));
            }
        };
    }

    private static CameraDetector.CameraDetectCallback newCameraDetectCallback(final SurfaceTexture surfaceTexture, final ProjectModule.StartPreviewListener startPreviewListener) {
        return new CameraDetector.CameraDetectCallback() {

            private String cameraErrorLog = "error";

            private void showCameraErrorLog() {
                MainThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        UiModule.cameraView0.setText(cameraErrorLog, Color.RED);
                    }
                });
            }
            @Override
            public void onDetect() {
                ThreadPool.execute(new Runnable() {
                    @Override
                    public void run() {

                        int i1 = CameraModule.initCamera();
                        if (i1 < 0) {
                            switch (i1) {
                                case -1:
                                    cameraErrorLog = Strings.打开相机出错();
                                    break;
                                case -2:
                                    cameraErrorLog = Strings.打开相机失败();
                                    break;
                                case -3:
                                    cameraErrorLog = Strings.相机异常();
                                    break;
                            }
                            showCameraErrorLog();
                            return;
                        }

                        int i2 = CameraModule.previewCamera(CameraModule.camera, surfaceTexture);
                        if (i2 < 0) {
                            switch (i2) {
                                case -1:
                                    cameraErrorLog = Strings.设置预览出错();
                                    break;
                                case -2:
                                    cameraErrorLog = Strings.预览相机出错();
                                    break;
                            }
                            showCameraErrorLog();
                            return;
                        }

                        //相机管理
                        CameraModule.manageCamera();

                        if (startPreviewListener != null)
                            startPreviewListener.onStartPreview(Camera.CameraInfo.CAMERA_FACING_BACK);
                    }
                });
            }

            @Override
            public void noDetect() {
                cameraErrorLog = Strings.未检测到主摄像头();
                showCameraErrorLog();
            }
        };
    }
}
