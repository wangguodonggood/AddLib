package com.topotek.libs.libs0.android.view.doublePreviewLayout;

import android.content.Context;
import android.view.View;

//ok
public class DynamicDoublePreviewLayout extends DoublePreviewLayout {

    protected DynamicDoublePreviewLayout(Context context) {
        super(context);
    }

    /**
     * 不检查参数
     */
    public DynamicDoublePreviewLayout(Context context, int smallChildGravity) {
        super(context);
        this.smallChildGravity = smallChildGravity;
    }

    /**
     * 不检查参数
     */
    public DynamicDoublePreviewLayout(Context context, View child0, int smallChild0Width, int smallChild0Height,
                                      View child1, int smallChild1Width, int smallChild1Height, int smallChildGravity) {
        super(context);

        this.smallChild0Width = smallChild0Width;
        this.smallChild0Height = smallChild0Height;
        this.smallChild1Width = smallChild1Width;
        this.smallChild1Height = smallChild1Height;
        this.smallChildGravity = smallChildGravity;

        if (child0 != null) {
            this.child0 = child0;
            addView(child0, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        }
        if (child1 != null) {
            this.child1 = child1;
            addView(child1, new LayoutParams(smallChild1Width, smallChild1Height, smallChildGravity));
        }
    }

    @Override
    protected void setChild0PreviewLayoutMode_BigSmall_01() {
        if (child0 != null)
            super.setChild0PreviewLayoutMode_BigSmall_01();
    }

    @Override
    protected void setChild1PreviewLayoutMode_BigSmall_01() {
        if (child1 != null)
            super.setChild1PreviewLayoutMode_BigSmall_01();
    }

    @Override
    protected void setChild0PreviewLayoutMode_Only_0() {
        if (child0 != null)
            super.setChild0PreviewLayoutMode_Only_0();
    }

    @Override
    protected void setChild1PreviewLayoutMode_Only_0() {
        if (child1 != null)
            super.setChild1PreviewLayoutMode_Only_0();
    }

    @Override
    protected void setChild0PreviewLayoutMode_BigSmall_10() {
        if (child0 != null)
            super.setChild0PreviewLayoutMode_BigSmall_10();
    }

    @Override
    protected void setChild1PreviewLayoutMode_BigSmall_10() {
        if (child1 != null)
            super.setChild1PreviewLayoutMode_BigSmall_10();
    }

    @Override
    protected void setChild0PreviewLayoutMode_Only_1() {
        if (child0 != null)
            super.setChild0PreviewLayoutMode_Only_1();
    }

    @Override
    protected void setChild1PreviewLayoutMode_Only_1() {
        if (child1 != null)
            super.setChild1PreviewLayoutMode_Only_1();
    }

    @Override
    protected void setChild0PreviewLayoutMode_LeftRight_01() {
        if (child0 != null)
            super.setChild0PreviewLayoutMode_LeftRight_01();
    }

    @Override
    protected void setChild1PreviewLayoutMode_LeftRight_01() {
        if (child1 != null)
            super.setChild1PreviewLayoutMode_LeftRight_01();
    }

    /**
     * @param previewLayoutMode 传人无效值则不响应
     */
    protected void setChild0PreviewLayoutMode(int previewLayoutMode) {
        switch (previewLayoutMode) {
            case PreviewLayoutMode_BigSmall_01:
                setChild0PreviewLayoutMode_BigSmall_01();
                break;
            case PreviewLayoutMode_Only_0:
                setChild0PreviewLayoutMode_Only_0();
                break;
            case PreviewLayoutMode_BigSmall_10:
                setChild0PreviewLayoutMode_BigSmall_10();
                break;
            case PreviewLayoutMode_Only_1:
                setChild0PreviewLayoutMode_Only_1();
                break;
            case PreviewLayoutMode_LeftRight_01:
                setChild0PreviewLayoutMode_LeftRight_01();
                break;
        }
    }

    /**
     * @param previewLayoutMode 传人无效值则不响应
     */
    protected void setChild1PreviewLayoutMode(int previewLayoutMode) {
        switch (previewLayoutMode) {
            case PreviewLayoutMode_BigSmall_01:
                setChild1PreviewLayoutMode_BigSmall_01();
                break;
            case PreviewLayoutMode_Only_0:
                setChild1PreviewLayoutMode_Only_0();
                break;
            case PreviewLayoutMode_BigSmall_10:
                setChild1PreviewLayoutMode_BigSmall_10();
                break;
            case PreviewLayoutMode_Only_1:
                setChild1PreviewLayoutMode_Only_1();
                break;
            case PreviewLayoutMode_LeftRight_01:
                setChild1PreviewLayoutMode_LeftRight_01();
                break;
        }
    }

    /**
     * 不检查参数
     */
    public void addChild0(View child0, int smallChild0Width, int smallChild0Height) {

        this.smallChild0Width = smallChild0Width;
        this.smallChild0Height = smallChild0Height;

        this.child0 = child0;
        addView(child0, 0);
        setChild0PreviewLayoutMode(currentPreviewLayoutMode);
    }

    /**
     * 不检查参数
     */
    public void addChild1(View child1, int smallChild1Width, int smallChild1Height) {

        this.smallChild1Width = smallChild1Width;
        this.smallChild1Height = smallChild1Height;

        this.child1 = child1;
        addView(child1, 0);
        setChild1PreviewLayoutMode(currentPreviewLayoutMode);
    }
}
