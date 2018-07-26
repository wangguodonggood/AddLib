package com.topotek.libs.libs1.android.view.cameraView;

import android.content.Context;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.View;
import android.widget.Chronometer;

import com.topotek.libs.libs0.android.view.childAdjustmentLayout.ChildAdjustmentLayout;

//ok
public class SimpleCameraView extends ChildAdjustmentLayout {
    protected CameraCaptureUiView mCameraCaptureUiView;
    protected Chronometer mChronometer;
    protected boolean isRecordStartUIing = false;
    protected boolean isCaptureStartUIing = false;
    protected float previewViewSizeScale = 1;
    /**
     * 不检查参数
     */
    protected SimpleCameraView(Context context) {
        super(context);
    }

    /**
     * 不检查参数
     */
    public SimpleCameraView(Context context, View previewView,
                            int cameraCaptureUiColor, int chronometerColor,
                            int chronometerGravity) {
        super(context);

        LayoutParams previewViewLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, Gravity.CENTER);
        addView(previewView, previewViewLayoutParams);

        mCameraCaptureUiView = new CameraCaptureUiView(context, cameraCaptureUiColor, 30F);
        mCameraCaptureUiView.setVisibility(INVISIBLE);
        addView(mCameraCaptureUiView);

        mChronometer = new Chronometer(context);
        mChronometer.setText(null);
        mChronometer.setTextColor(chronometerColor);
        LayoutParams chronometerLayoutParams
                = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, chronometerGravity);
        addView(mChronometer, chronometerLayoutParams);
    }

    public void captureStartUI() {
        mCameraCaptureUiView.setVisibility(VISIBLE);
        isCaptureStartUIing = true;
    }


    public void captureFinishUI() {
        mCameraCaptureUiView.setVisibility(INVISIBLE);
        isCaptureStartUIing = false;
    }

    public void captureUi() {
        if (isCaptureStartUIing)
            captureFinishUI();
        else
            captureStartUI();
    }

    /**
     * 会检查状态
     */
    public void recordStartUI() {

        if (isRecordStartUIing)
            return;

        mChronometer.setFormat("%s");
        mChronometer.setBase(SystemClock.elapsedRealtime());
        mChronometer.start();

        isRecordStartUIing = true;
    }

    /**
     * 会检查状态
     */
    public void recordFinishUI() {

        if (!isRecordStartUIing)
            return;

        mChronometer.stop();
        mChronometer.setText(null);

        isRecordStartUIing = false;
    }

    public void recordUI() {
        if (isRecordStartUIing)
            recordFinishUI();
        else
            recordStartUI();
    }

    public void setPreviewViewSizeScale(float scale) {
        previewViewSizeScale = scale;
        setChildSizeScaleAsParent(0, previewViewSizeScale);
    }

    public void plusPreviewViewSizeScale(float plus) {
        previewViewSizeScale += plus;
        setChildSizeScaleAsParent(0, previewViewSizeScale);
    }

    public void setPreviewViewTranslationX(float translationX) {
        setChildTranslationX(0, translationX);
    }

    public void setPreviewViewTranslationY(float translationY) {
        setChildTranslationY(0, translationY);
    }

    public void plusPreviewViewTranslationX(float plus) {
        plusChildTranslationX(0, plus);
    }

    public void plusPreviewViewTranslationY(float plus) {
        plusChildTranslationY(0, plus);
    }
}
