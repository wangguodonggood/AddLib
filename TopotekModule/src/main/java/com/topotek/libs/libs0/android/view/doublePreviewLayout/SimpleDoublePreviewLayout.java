package com.topotek.libs.libs0.android.view.doublePreviewLayout;

import android.content.Context;
import android.view.Gravity;
import android.view.View;

//ok
public class SimpleDoublePreviewLayout extends SetChildWHGFrameLayout {

    protected View child0;
    protected int smallChild0Width;
    protected int smallChild0Height;
    protected View child1;
    protected int smallChild1Width;
    protected int smallChild1Height;

    protected int smallChildGravity;

    protected SimpleDoublePreviewLayout(Context context) {
        super(context);
    }

    /**
     * 不检查参数
     */
    public SimpleDoublePreviewLayout(Context context,
                                     View child0,
                                     int smallChild0Width,
                                     int smallChild0Height,
                                     View child1,
                                     int smallChild1Width,
                                     int smallChild1Height,
                                     int smallChildGravity) {
        super(context);
        this.child0 = child0;
        this.smallChild0Width = smallChild0Width;
        this.smallChild0Height = smallChild0Height;
        this.child1 = child1;
        this.smallChild1Width = smallChild1Width;
        this.smallChild1Height = smallChild1Height;
        this.smallChildGravity = smallChildGravity;

        addView(child0, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        addView(child1, new LayoutParams(smallChild1Width, smallChild1Height, smallChildGravity));
    }

    protected void setChild0PreviewLayoutMode_BigSmall_01() {
        setViewWH(child0, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    protected void setChild1PreviewLayoutMode_BigSmall_01() {
        setViewWHG(child1, smallChild1Width, smallChild1Height, smallChildGravity);
        child1.bringToFront();
    }

    public void setPreviewLayoutMode_BigSmall_01() {
        setChild0PreviewLayoutMode_BigSmall_01();
        setChild1PreviewLayoutMode_BigSmall_01();
    }

    protected void setChild0PreviewLayoutMode_Only_0() {
        setViewWH(child0, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        child0.bringToFront();
    }

    protected void setChild1PreviewLayoutMode_Only_0() {

    }

    public void setPreviewLayoutMode_Only_0() {
        setChild0PreviewLayoutMode_Only_0();
        setChild1PreviewLayoutMode_Only_0();
    }

    protected void setChild0PreviewLayoutMode_BigSmall_10() {
        setViewWHG(child0, smallChild0Width, smallChild0Height, smallChildGravity);
        child0.bringToFront();
    }

    protected void setChild1PreviewLayoutMode_BigSmall_10() {
        setViewWH(child1, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    public void setPreviewLayoutMode_BigSmall_10() {
        setChild0PreviewLayoutMode_BigSmall_10();
        setChild1PreviewLayoutMode_BigSmall_10();
    }

    protected void setChild0PreviewLayoutMode_Only_1() {

    }

    protected void setChild1PreviewLayoutMode_Only_1() {
        setViewWH(child1, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        child1.bringToFront();
    }

    public void setPreviewLayoutMode_Only_1() {
        setChild0PreviewLayoutMode_Only_1();
        setChild1PreviewLayoutMode_Only_1();
    }

    protected void setChild0PreviewLayoutMode_LeftRight_01() {
        int layoutWidth = getWidth();
        int childWidth = layoutWidth / 2;
        setViewWHG(child0, childWidth, LayoutParams.MATCH_PARENT, Gravity.LEFT);
        child0.bringToFront();
        //因为特殊情况: HDMI输出的画面中，surfaceView超出父布局部分也可见
    }

    protected void setChild1PreviewLayoutMode_LeftRight_01() {
        int layoutWidth = getWidth();
        int childWidth = layoutWidth / 2;
        setViewWHG(child1, childWidth, LayoutParams.MATCH_PARENT, Gravity.RIGHT);
    }

    public void setPreviewLayoutMode_LeftRight_01() {
        setChild0PreviewLayoutMode_LeftRight_01();
        setChild1PreviewLayoutMode_LeftRight_01();
    }

//    protected void setChild0PreviewLayoutMode_LeftRight_10() {
//        int layoutWidth = getWidth();
//        int childWidth = layoutWidth / 2;
//        setViewWHG(child0, childWidth, LayoutParams.MATCH_PARENT, Gravity.RIGHT);
//        child0.bringToFront();//因为特殊情况: HDMI输出的画面中，surfaceView超出父布局部分也可见
//    }
//
//    protected void setChild1PreviewLayoutMode_LeftRight_10() {
//        int layoutWidth = getWidth();
//        int childWidth = layoutWidth / 2;
//        setViewWHG(child1, childWidth, LayoutParams.MATCH_PARENT, Gravity.LEFT);
//    }
//
//    public void setPreviewLayoutMode_LeftRight_10() {
//        setChild0PreviewLayoutMode_LeftRight_10();
//        setChild1PreviewLayoutMode_LeftRight_10();
//    }

//    protected void setChild0PreviewLayoutMode_Superposition_01() {
//        setViewWH(child0, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//    }
//
//    protected void setChild1PreviewLayoutMode_Superposition_01() {
//        setViewWH(child1, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//        child1.bringToFront();
//    }
//
//    public void setPreviewLayoutMode_Superposition_01() {
//        setChild0PreviewLayoutMode_Superposition_01();
//        setChild1PreviewLayoutMode_Superposition_01();
//    }
    protected void setChild0PreviewLayoutMode_Superposition_10() {
        setViewWH(child0, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        child0.bringToFront();
    }
    protected void setChild1PreviewLayoutMode_Superposition_10() {
        setViewWH(child1, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }
    public void setPreviewLayoutMode_Superposition_10() {
        setChild0PreviewLayoutMode_Superposition_10();
        setChild1PreviewLayoutMode_Superposition_10();
    }
}