package com.topotek.topotekmodule.SimplifyCallback;

import android.graphics.SurfaceTexture;
import android.view.TextureView;

import com.topotek.module.android.debug.LogManager;
//重写监听 onSurfaceTextureDestroyed时之间关闭系统
public abstract class MySurfaceTextureListener implements TextureView.SurfaceTextureListener {

    @Override
    public abstract void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height);

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        LogManager.log("TextureView  onSurfaceTextureDestroyed    --->  System.exit(1)");
        System.exit(1);
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

    }
}
