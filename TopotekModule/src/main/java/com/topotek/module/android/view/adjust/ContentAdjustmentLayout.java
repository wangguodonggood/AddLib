package com.topotek.module.android.view.adjust;

import android.content.Context;
import android.view.View;

public class ContentAdjustmentLayout extends ContentScaleLayout {

    public ContentAdjustmentLayout(Context context) {
        super(context);
    }

    public static void setViewTranslationX(View view, float translationX) {
        view.setTranslationX(translationX);
    }

    public static void setViewTranslationY(View view, float translationY) {
        view.setTranslationY(translationY);
    }

    public static void plusViewTranslationX(View view, float x) {
        float translationX = view.getTranslationX();
        view.setTranslationX(translationX + x);
    }

    public static void plusViewTranslationY(View view, float y) {
        float translationY = view.getTranslationY();
        view.setTranslationY(translationY + y);
    }

    public void setChildTranslationX(int childIndex, float translationX) {
        View child = getChildAt(childIndex);
        setViewTranslationX(child, translationX);
    }

    public void setChildTranslationY(int childIndex, float translationY) {
        View child = getChildAt(childIndex);
        setViewTranslationY(child, translationY);
    }

    public void plusChildTranslationX(int childIndex, float x) {
        View child = getChildAt(childIndex);
        plusViewTranslationX(child, x);
    }

    public void plusChildTranslationY(int childIndex, float y) {
        View child = getChildAt(childIndex);
        plusViewTranslationY(child, y);
    }
}
