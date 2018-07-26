package com.topotek.libs.libs1.android.view.cameraView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

//ok
public class CameraCaptureUiView extends View {

    protected Paint mPaint;
    protected float captureUiSize;

    /**
     * 不检查参数
     */
    public CameraCaptureUiView(Context context, int cameraCaptureUiColor, float captureUiSize) {
        super(context);

        mPaint = new Paint();
        mPaint.setColor(cameraCaptureUiColor);
        mPaint.setStyle(Paint.Style.FILL);

        this.captureUiSize = captureUiSize;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();

        canvas.drawRect(0, 0, captureUiSize, height, mPaint);
        canvas.drawRect(0, 0, width, captureUiSize, mPaint);
        canvas.drawRect(width - captureUiSize, 0, width, height, mPaint);
        canvas.drawRect(0, height - captureUiSize, width, height, mPaint);
    }
}
