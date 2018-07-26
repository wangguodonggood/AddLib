package com.topotek.libs.libs0.android.view.childAdjustmentLayout;

import android.content.Context;
import android.view.View;

//在缩放的基础上增加了平移
public class ChildAdjustmentLayout extends ChildSizeScaleFrameLayout {
    /**
     * 不检查参数
     */
    public ChildAdjustmentLayout(Context context) {
        super(context);
    }

    /**
     * 将子控件沿着X轴平移
     */
    public void setChildTranslationX(int childIndex, float translationX) {
        View child = getChildAt(childIndex);
        child.setTranslationX(translationX);
    }

    /**
     * 将子控件沿着Y轴平移
     */
    public void setChildTranslationY(int childIndex, float translationY) {
        View child = getChildAt(childIndex);
        child.setTranslationY(translationY);
    }

    /**
     * 不检查参数
     */
    public static void plusViewTranslationX(View view, float x) {
        float translationX = view.getTranslationX();
        view.setTranslationX(translationX + x);
    }

    /**
     * 不检查参数
     */
    public static void plusViewTranslationY(View view, float y) {
        float translationY = view.getTranslationY();
        view.setTranslationY(translationY + y);
    }

    /**
     * 不检查参数
     */
    public void plusChildTranslationX(int childIndex, float x) {
        View child = getChildAt(childIndex);
        plusViewTranslationX(child, x);
    }

    /**
     * 不检查参数
     */
    public void plusChildTranslationY(int childIndex, float y) {
        View child = getChildAt(childIndex);
        plusViewTranslationY(child, y);
    }
}
