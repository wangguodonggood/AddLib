package com.topotek.module.android.cameraView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class CaptureUiView extends View {

    private Paint mPaint = new Paint();
    private float captureUiSize;

    public CaptureUiView(Context context, float captureUiSize) {
        super(context);

        mPaint.setColor(Color.WHITE);
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
