package com.topotek.module.android.cameraView;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import com.topotek.module.android.debug.Debugger;

public class PreviewViewEncapsulation extends FrameLayout {

    private AimView aimView;

    public static PreviewViewEncapsulation create(Context context, View previewView, int midpointType) {
        return new PreviewViewEncapsulation(context, previewView, midpointType);
    }

    private PreviewViewEncapsulation(Context context, View previewView, int midpointType) {
        super(context);

//        FrameLayout frameLayout = new FrameLayout(context);
//        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, Gravity.CENTER);
//        addView(frameLayout, layoutParams);
        addView(previewView);
        aimView = new AimView(context, midpointType);
        addView(aimView);
    }
    public void setMidpoint(int i){
        aimView.setMidpointType(i);
    }
}
