package com.topotek.module.android.view.adjust;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class ContentScaleLayout extends FrameLayout {

    public ContentScaleLayout(Context context) {
        super(context);
    }

    public static void setViewSize(View view, int width, int height) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height;
        view.setLayoutParams(layoutParams);
    }

    public static void setViewScale(View view, int width, int height, float scale) {
        width = (int) (width * scale);
        height = (int) (height * scale);
        setViewSize(view, width, height);
    }

    public static void setViewScale(View view, float scale) {
        int width = view.getWidth();
        int height = view.getHeight();
        setViewScale(view, width, height, scale);
    }

    public void setChildSize(int childIndex, int width, int height) {
        View child = getChildAt(childIndex);
        setViewSize(child, width, height);
    }

    public void setChildScale(int childIndex, int width, int height, float scale) {
        View child = getChildAt(childIndex);
        setViewScale(child, width, height, scale);
    }

    public void setChildScale(int childIndex, float scale) {
        View child = getChildAt(childIndex);
        setViewScale(child, scale);
    }

    public void setChildScaleAsParent(int childIndex, float scale) {
        int width = getWidth();
        int height = getHeight();
        setChildScale(childIndex, width, height, scale);
    }
}
