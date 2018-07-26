package com.topotek.libs.libs0.android.view.doublePreviewLayout;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

//ok
public class SetChildWHGFrameLayout extends FrameLayout {

    public SetChildWHGFrameLayout(Context context) {
        super(context);
    }

    /**
     * 不检查参数
     */
    public static void setLayoutParamsWH(ViewGroup.LayoutParams layoutParams, int width, int height) {
        layoutParams.width = width;
        layoutParams.height = height;
    }

    /**
     * 不检查参数
     */
    public static void setLayoutParamsWHG(LayoutParams frameLayoutLayoutParams, int width, int height, int gravity) {
        setLayoutParamsWH(frameLayoutLayoutParams, width, height);
        frameLayoutLayoutParams.gravity = gravity;
    }

    /**
     * 不检查参数
     */
    public static void setViewWH(View view, int width, int height) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        setLayoutParamsWH(layoutParams, width, height);
        view.setLayoutParams(layoutParams);
    }

    /**
     * 不检查参数
     */
    public static void setViewWHG(View view, int width, int height, int gravity) {
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        setLayoutParamsWHG(layoutParams, width, height, gravity);
        view.setLayoutParams(layoutParams);
    }

    /**
     * 不检查参数
     */
    public void setChildWH(int childIndex, int width, int height) {
        View child = getChildAt(childIndex);
        setViewWH(child, width, height);
    }

    /**
     * 不检查参数
     */
    public void setChildWHG(int childIndex, int width, int height, int gravity) {
        View child = getChildAt(childIndex);
        setViewWHG(child, width, height, gravity);
    }
}
