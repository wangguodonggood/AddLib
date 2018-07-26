package com.topotek.topotekmodule.SimplifyCallback;

import android.view.SurfaceHolder;

import com.topotek.module.android.debug.LogManager;

public abstract class MySurfaceHolderCallback implements SurfaceHolder.Callback {

    @Override
    public abstract void surfaceCreated(SurfaceHolder holder);

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        LogManager.log("SurfaceView  surfaceDestroyed    --->  System.exit(1)");
        System.exit(1);
    }
}
