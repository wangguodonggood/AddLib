package com.topotek.topotekmodule.uiModule;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;

import com.topotek.libs.libs0.android.view.doublePreviewLayout.DynamicDoublePreviewLayout;
import com.topotek.libs.libs1.android.view.cameraView.CameraView;
import com.topotek.module.android.cameraView.PreviewViewEncapsulation;
import com.topotek.module.android.menu.Menu;
import com.topotek.module.project.uiView.UiLayout;
import com.topotek.topotekmodule.menu.MenuModule;

public class UiModule {
    public static TextureView previewView0;
    public static SurfaceView previewView1;
    public static PreviewViewEncapsulation previewViewEncapsulation0;
    public static PreviewViewEncapsulation previewViewEncapsulation1;
    public static CameraView cameraView0;
    public static CameraView cameraView1;
    public static DynamicDoublePreviewLayout dynamicDoublePreviewLayout;
    public static UiLayout uiLayout;

    public static Menu menu;
    public static UiLayout initUiView(Context context) {
        dynamicDoublePreviewLayout = new DynamicDoublePreviewLayout(context, Gravity.RIGHT);
        uiLayout = UiLayout.create(context, dynamicDoublePreviewLayout);
        menu = MenuModule.createMenuModule(context);
        return uiLayout;
    }
    //添加主摄像头
    public static void addCameraView0(Context context,
                                      TextureView.SurfaceTextureListener
                                              surfaceTextureListener) {
        //预览视频的View
        previewView0 = new TextureView(context);
        previewView0.setKeepScreenOn(true);
        //监听摄像头是否可用
        previewView0.setSurfaceTextureListener(surfaceTextureListener);
        //如果向预览View里添加组件时需要添加
        previewViewEncapsulation0 = PreviewViewEncapsulation.create(context, previewView0, 0);
//        cameraView0 = CameraView.create(context, previewViewEncapsulation0, 320, 180);
        cameraView0 = new CameraView(context, previewViewEncapsulation0, 320,
                180, Color.WHITE, Color.RED, Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        dynamicDoublePreviewLayout.addChild0(cameraView0, 320, 180);
    }
    //添加USB摄像头
    public static void addCameraView1(Context context, SurfaceHolder.Callback callback) {
        previewView1 = new SurfaceView(context);
        previewView1.getHolder().addCallback(callback);
        previewViewEncapsulation1 = PreviewViewEncapsulation.create(context, previewView1, 0);
//        cameraView1 = CameraView.create(context, previewViewEncapsulation1, 240, 180);
        cameraView1 = new CameraView(context, previewViewEncapsulation1, 240, 180, Color.WHITE, Color.RED, Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        dynamicDoublePreviewLayout.addChild1(cameraView1, 320, 240);
    }
}
