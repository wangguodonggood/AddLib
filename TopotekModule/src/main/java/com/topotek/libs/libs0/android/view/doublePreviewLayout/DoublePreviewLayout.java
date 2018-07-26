package com.topotek.libs.libs0.android.view.doublePreviewLayout;

import android.content.Context;
import android.view.View;

//ok
public class DoublePreviewLayout extends SimpleDoublePreviewLayout {

    public static final int PreviewLayoutMode_BigSmall_01 = 0;
    public static final int PreviewLayoutMode_Only_0 = 1;
    public static final int PreviewLayoutMode_BigSmall_10 = 2;
    public static final int PreviewLayoutMode_Only_1 = 3;
    public static final int PreviewLayoutMode_LeftRight_01 = 4;

    public static final int PreviewLayoutModeCount = 5;

    protected int currentPreviewLayoutMode = PreviewLayoutMode_BigSmall_01;

    protected DoublePreviewLayout(Context context) {
        super(context);
    }

    /**
     * 不检查参数
     */
    public DoublePreviewLayout(Context context, View child0, int smallChild0Width, int smallChild0Height,
                               View child1, int smallChild1Width, int smallChild1Height, int smallChildGravity) {
        super(context, child0, smallChild0Width, smallChild0Height, child1, smallChild1Width, smallChild1Height, smallChildGravity);
    }

    public int getCurrentPreviewLayoutMode() {
        return currentPreviewLayoutMode;
    }

    @Override
    public void setPreviewLayoutMode_BigSmall_01() {
        super.setPreviewLayoutMode_BigSmall_01();
        currentPreviewLayoutMode = PreviewLayoutMode_BigSmall_01;
    }

    @Override
    public void setPreviewLayoutMode_Only_0() {
        super.setPreviewLayoutMode_Only_0();
        currentPreviewLayoutMode = PreviewLayoutMode_Only_0;
    }

    @Override
    public void setPreviewLayoutMode_BigSmall_10() {
        super.setPreviewLayoutMode_BigSmall_10();
        currentPreviewLayoutMode = PreviewLayoutMode_BigSmall_10;
    }

    @Override
    public void setPreviewLayoutMode_Only_1() {
        super.setPreviewLayoutMode_Only_1();
        currentPreviewLayoutMode = PreviewLayoutMode_Only_1;
    }

    @Override
    public void setPreviewLayoutMode_LeftRight_01() {
        super.setPreviewLayoutMode_LeftRight_01();
        currentPreviewLayoutMode = PreviewLayoutMode_LeftRight_01;
    }

    /**
     * @param previewLayoutMode 传人无效值则不响应
     */
    public void setPreviewLayoutMode(int previewLayoutMode) {
        switch (previewLayoutMode) {
            case PreviewLayoutMode_BigSmall_01:
                setPreviewLayoutMode_BigSmall_01();
                break;
            case PreviewLayoutMode_Only_0:
                setPreviewLayoutMode_Only_0();
                break;
            case PreviewLayoutMode_BigSmall_10:
                setPreviewLayoutMode_BigSmall_10();
                break;
            case PreviewLayoutMode_Only_1:
                setPreviewLayoutMode_Only_1();
                break;
            case PreviewLayoutMode_LeftRight_01:
                setPreviewLayoutMode_LeftRight_01();
                break;
        }
    }

    public void nextPreviewLayoutMode() {
        if (currentPreviewLayoutMode == PreviewLayoutModeCount - 1)
            setPreviewLayoutMode(0);
        else
            setPreviewLayoutMode(currentPreviewLayoutMode + 1);
    }

    public void previousPreviewLayoutMode() {
        if (currentPreviewLayoutMode == 0)
            setPreviewLayoutMode(PreviewLayoutModeCount - 1);
        else
            setPreviewLayoutMode(currentPreviewLayoutMode - 1);
    }
}
