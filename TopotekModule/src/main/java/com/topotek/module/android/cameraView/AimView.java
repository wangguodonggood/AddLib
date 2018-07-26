package com.topotek.module.android.cameraView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class AimView extends View {

    private int midpointType;
    private Paint mPaint = new Paint();

    public AimView(Context context, int midpointType) {
        super(context);

        mPaint.setStrokeWidth(3);
        mPaint.setColor(Color.GREEN);
        mPaint.setStyle(Paint.Style.FILL);

        this.midpointType = midpointType;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();

        switch (midpointType) {
            case 0:
                break;
            case 1:
                drawMidpoint_Type1(canvas, width, height, 50, 20);
                break;
            case 2:
                drawMidpoint_Type2(canvas, width, height);
                break;
            case 3:
                drawMidpoint_Type3(canvas, width, height, 40);
                break;
            case 4:
                drawMidpoint_Type4(canvas, width, height, 50, 20);
                break;
        }
    }

    private void drawMidpoint_Type1(Canvas canvas, int width, int height, int a, int z) {
        canvas.drawLine(width / 2, height / 2 - a, width / 2, height / 2 - z, mPaint);
        canvas.drawLine(width / 2, height / 2 + a, width / 2, height / 2 + z, mPaint);
        canvas.drawLine(width / 2 - a, height / 2, width / 2 - z, height / 2, mPaint);
        canvas.drawLine(width / 2 + a, height / 2, width / 2 + z, height / 2, mPaint);
    }

    private void drawMidpoint_Type2(Canvas canvas, int width, int height){
        canvas.drawCircle(width / 2, height / 2, 3, mPaint);
    }

    private void drawMidpoint_Type3(Canvas canvas, int width, int height, int a){
        canvas.drawLine(width / 2, height / 2 - a, width / 2, height / 2 + a, mPaint);
        canvas.drawLine(width / 2 - a, height / 2, width / 2 + a, height / 2, mPaint);
    }

    private void drawMidpoint_Type4(Canvas canvas, int width, int height, int a, int z){
        drawMidpoint_Type1(canvas, width, height, a, z);
        drawMidpoint_Type2(canvas, width, height);
    }

    public void setMidpointType(int midpointType) {
        this.midpointType = midpointType;
        invalidate();
    }
}
