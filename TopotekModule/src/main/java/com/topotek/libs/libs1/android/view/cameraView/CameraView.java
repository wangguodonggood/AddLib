package com.topotek.libs.libs1.android.view.cameraView;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

//ok
public class CameraView extends SimpleCameraView {

    protected int previewWidth;
    protected int previewHeight;
    protected TextView mTextView;
    /**
     * 不检查参数
     */
    protected CameraView(Context context) {
        super(context);
    }

    /**
     * 不检查参数
     */
    public CameraView(Context context, View previewView, int previewWidth, int previewHeight,
                      int cameraCaptureUiColor, int chronometerColor, int chronometerGravity) {
        super(context, previewView, cameraCaptureUiColor, chronometerColor, chronometerGravity);
        this.previewWidth = previewWidth;
        this.previewHeight = previewHeight;
    }

    //自动匹配不同的模式显示布局
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        int width = getWidth();
        int height = getHeight();

        int childWidth;
        int childHeight;
        int childW = previewWidth * height / previewHeight;
        if (childW == width) {
            childWidth = width;
            childHeight = height;
        } else if (childW < width) {
            childWidth = width;
            childHeight = width * previewHeight / previewWidth;
        } else {
            childWidth = childW;
            childHeight = height;
        }

        int childScaleWidth = (int) (childWidth * previewViewSizeScale);
        int childScaleHeight = (int) (childHeight * previewViewSizeScale);

        View child = getChildAt(0);
        int childMeasuredWidth = child.getMeasuredWidth();
        int childMeasuredHeight = child.getMeasuredHeight();

        if (childMeasuredWidth != childScaleWidth || childMeasuredHeight != childScaleHeight) {
            setViewSize(child, childScaleWidth, childScaleHeight);
        }
    }

    public float getPreviewViewSizeScale() {
        return previewViewSizeScale;
    }

    /**
     * 不检查参数
     */
    @Override
    public void setPreviewViewSizeScale(float scale) {
        previewViewSizeScale = scale;
        requestLayout();
    }

    /**
     * 不检查参数
     */
    @Override
    public void plusPreviewViewSizeScale(float plus) {
        previewViewSizeScale += plus;
        requestLayout();
    }

    /**
     * 不检查参数
     */
    public void setText(CharSequence text, int color) {

        if (mTextView == null) {
            mTextView = new TextView(getContext());
            mTextView.setGravity(Gravity.CENTER);
            LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.CENTER);
            addView(mTextView, layoutParams);
        }

        mTextView.setTextColor(color);
        mTextView.setText(text);
    }
}
