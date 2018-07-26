package com.topotek.module.android.cameraView;

import android.content.Context;
import android.graphics.Color;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.View;
import android.widget.Chronometer;

import com.topotek.module.android.view.adjust.ContentAdjustmentLayout;

public class CameraView extends ContentAdjustmentLayout {

    private CaptureUiView mCaptureUiView;
    private Chronometer mChronometer;
    private boolean isRecordStartUIing = false;

    private float previewViewScale = 1;

    public static CameraView create(Context context, View previewView, int previewWidth, int previewHeight) {
        return new CameraView(context, previewView, previewWidth, previewHeight);
    }

    private CameraView(Context context, View previewView, int previewWidth, int previewHeight) {
        super(context);

        LayoutParams previewViewLayoutParams = new LayoutParams(previewWidth, previewHeight, Gravity.CENTER);
        addView(previewView, previewViewLayoutParams);

        mCaptureUiView = new CaptureUiView(context, 30F);
        mCaptureUiView.setVisibility(INVISIBLE);
        addView(mCaptureUiView);

        mChronometer = new Chronometer(context);
        mChronometer.setText(null);
        mChronometer.setTextColor(Color.RED);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.CENTER_VERTICAL | Gravity.RIGHT);
        addView(mChronometer, layoutParams);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        int childCount = getChildCount();
        if (childCount > 2) {

            int width = getWidth();
            int height = getHeight();
            View child = getChildAt(0);
            int childMeasuredWidth = child.getMeasuredWidth();
            int childMeasuredHeight = child.getMeasuredHeight();

            int childWidth;
            int childHeight;
            int childW = childMeasuredWidth * height / childMeasuredHeight;
            if (childW == width) {
                childWidth = width;
                childHeight = height;
            } else if (childW < width) {
                childWidth = width;
                childHeight = width * childMeasuredHeight / childMeasuredWidth;
            } else {
                childWidth = childW;
                childHeight = height;
            }

//            if (childMeasuredWidth != childWidth || childMeasuredHeight != childHeight) {
//                ViewGroup.LayoutParams childLayoutParams = child.getLayoutParams();
//                childLayoutParams.width = childWidth;
//                childLayoutParams.height = childHeight;
//                child.setLayoutParams(childLayoutParams);
//            }
            int childScaleWidth = (int) (childWidth * previewViewScale);
            int childScaleHeight = (int) (childHeight * previewViewScale);
            if (childMeasuredWidth != childScaleWidth || childMeasuredHeight != childScaleHeight) {
                setViewSize(child, childScaleWidth, childScaleHeight);
            }
        }
    }

    public void setPreviewViewScale(float scale) {
        previewViewScale = scale;
        requestLayout();
    }

    public void plusPreviewViewScale(float plus) {
        previewViewScale += plus;
        requestLayout();
    }

    public void setPreviewViewTranslationX(float translationX) {
        setChildTranslationX(0, translationX);
    }

    public void setPreviewViewTranslationY(float translationY) {
        setChildTranslationY(0, translationY);
    }

    public void plusPreviewViewTranslationX(float x) {
        plusChildTranslationX(0, x);
    }

    public void plusPreviewViewTranslationY(float y) {
        plusChildTranslationY(0, y);
    }

    /**
     * 线程不安全
     */
    public void pictureStartUI() {
        mCaptureUiView.setVisibility(VISIBLE);
    }

    /**
     * 线程不安全
     */
    public void pictureFinishUI() {
        mCaptureUiView.setVisibility(INVISIBLE);
    }

    /**
     * 线程不安全
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
     * 线程不安全
     */
    public void recordFinishUI() {

        if (!isRecordStartUIing)
            return;

        mChronometer.stop();
        mChronometer.setText(null);

        isRecordStartUIing = false;
    }

    /**
     * 线程不安全
     */
    public void recordUI() {
        if (isRecordStartUIing)
            recordFinishUI();
        else
            recordStartUI();
    }

    public void setText(CharSequence text) {

        LayoutParams layoutParams = (LayoutParams) mChronometer.getLayoutParams();
        layoutParams.gravity = Gravity.CENTER;
        mChronometer.setLayoutParams(layoutParams);

        mChronometer.setText(text);
    }

    public void cameraErrorUI() {
        setBackgroundColor(Color.RED);
        setPadding(0, 0, 1920, 1080);
    }
}
