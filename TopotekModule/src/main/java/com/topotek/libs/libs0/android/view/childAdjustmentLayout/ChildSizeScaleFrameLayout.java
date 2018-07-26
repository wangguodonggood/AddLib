package com.topotek.libs.libs0.android.view.childAdjustmentLayout;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

//ok最里层是一个帧布局
public class ChildSizeScaleFrameLayout extends FrameLayout {

    /**
     * 不检查参数
     */
    public ChildSizeScaleFrameLayout(Context context) {
        super(context);
    }
    /**
     * 设置布局的宽高
     */
    public static void setLayoutParamsWidthHeight(ViewGroup.LayoutParams layoutParams, int width, int height) {
        layoutParams.width = width;
        layoutParams.height = height;
    }
    /**
     * 不检查参数
     */
    public static void setViewSize(View view, int width, int height) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        setLayoutParamsWidthHeight(layoutParams, width, height);
        view.setLayoutParams(layoutParams);
    }

    /**
     * 不检查参数
     */
    public void setChildSize(int childIndex, int width, int height) {
        View child = getChildAt(childIndex);
        setViewSize(child, width, height);
    }

    /**
     * 根
     */
    public static void setLayoutParamsWidthHeightScale
     (ViewGroup.LayoutParams layoutParams, int width, int height, float scale) {
        layoutParams.width = (int) (width * scale);
        layoutParams.height = (int) (height * scale);
    }

    /**
     * 设置组建缩放  width增加为scale倍  height扩增为scale倍
     */
    public static void setViewSizeScale(View view, int width, int height, float scale) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        setLayoutParamsWidthHeightScale(layoutParams, width, height, scale);
        view.setLayoutParams(layoutParams);
    }

    /**
     * 根据宽高直接设置为当前宽高的几倍
     */
    public void setChildSizeScale(int childIndex, int width, int height, float scale) {
        View child = getChildAt(childIndex);
        setViewSizeScale(child, width, height, scale);
    }

    /**
     * 根据组建的宽高 缩放
     */
    public static void setViewSizeScale(View view, float scale) {
        int viewWidth = view.getWidth();
        int viewHeight = view.getHeight();
        setViewSizeScale(view, viewWidth, viewHeight, scale);
    }

    /**
     * 设置子控件缩放
     */
    public void setChildSizeScale(int childIndex, float scale) {
        View child = getChildAt(childIndex);
        setViewSizeScale(child, scale);
    }

    /**
     * 根据父容器的大小 扩增上scale倍
     */
    public void setChildSizeScaleAsParent(int childIndex, float scale) {
        int parentWidth = getWidth();
        int parentHeight = getHeight();
        setChildSizeScale(childIndex, parentWidth, parentHeight, scale);
    }
}
