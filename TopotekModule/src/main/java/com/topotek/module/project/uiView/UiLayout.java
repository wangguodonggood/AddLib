package com.topotek.module.project.uiView;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
//最外层的主布局---》动态双预览
public class UiLayout extends FrameLayout {

    private static final int ICON_WIDTH = 50;
    private static final int ICON_HEIGHT = 50;

    private static final int UI_MARGIN_RIGHT = 50;

    public static final int UI_STATE_OK = 1;
    public static final int UI_STATE_ELSE = 0;
    public static final int UI_STATE_ERROR = -1;

    private LinearLayout leftDownUiLayout;
    private ImageView tfCardIconImageView;
    private TextView tfCardAvailableSizeTextView;

    private LinearLayout leftUpUiLayout;
    private ImageView gpsIconImageView;
    private TextView gpsLongitudeTextView;
    private TextView gpsLatitudeTextView;
    private TextView distanceTextView;

    private TextView textView;

    public static UiLayout create(Context context, View cameraViewLayout) {
        return new UiLayout(context, cameraViewLayout);
    }

    private UiLayout(Context context, View cameraViewLayout) {
        super(context);
        addView(cameraViewLayout);

        leftUpUiLayout = new LinearLayout(context);
        LayoutParams layoutParams1
                = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        addView(leftUpUiLayout, layoutParams1);
        for (int i = 1; i <= 1; ++i) {
            LinearLayout linearLayout = new LinearLayout(context);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            leftUpUiLayout.addView(linearLayout, lp);
        }

        tfCardIconImageView = new ImageView(context);
        tfCardAvailableSizeTextView = new TextView(context);
        tfCardAvailableSizeTextView.setGravity(Gravity.CENTER_VERTICAL);

        leftDownUiLayout = new LinearLayout(context);
        LayoutParams layoutParams2 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.BOTTOM);
        addView(leftDownUiLayout, layoutParams2);
        for (int i = 1; i <= 1; ++i) {
            LinearLayout linearLayout = new LinearLayout(context);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            leftDownUiLayout.addView(linearLayout, lp);
        }

        gpsIconImageView = new ImageView(context);
        gpsIconImageView.setImageResource(android.R.drawable.ic_menu_mylocation);
        gpsLongitudeTextView = new TextView(context);
        gpsLongitudeTextView.setGravity(Gravity.CENTER_VERTICAL);
        gpsLatitudeTextView = new TextView(context);
        gpsLatitudeTextView.setGravity(Gravity.CENTER_VERTICAL);

        distanceTextView = new TextView(context);
        distanceTextView.setTextColor(Color.GREEN);
        LayoutParams layoutParams3 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
        addView(distanceTextView, layoutParams3);

//        setGpsUiIsShow(true);//显示gps----------================-------=======---
    }

    public void setDistanceText(CharSequence text){
        distanceTextView.setText(text);
    }

    public void setTfCardUiIsShow(boolean isShow) {

        LinearLayout linearLayout = (LinearLayout) leftDownUiLayout.getChildAt(0);

        if (isShow) {
            if (tfCardIconImageView.getParent() == null)
                linearLayout.addView(tfCardIconImageView, ICON_WIDTH, ICON_HEIGHT);
            if (tfCardAvailableSizeTextView.getParent() == null) {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, ICON_HEIGHT);
                layoutParams.setMargins(0, 0, UI_MARGIN_RIGHT, 0);
                linearLayout.addView(tfCardAvailableSizeTextView, layoutParams);
            }
        } else {
            linearLayout.removeAllViews();
        }
    }

    public void setTfCardUiState(int state) {
        switch (state) {
            case UI_STATE_OK:
                tfCardIconImageView.setImageResource(android.R.drawable.stat_notify_sdcard);
                tfCardAvailableSizeTextView.setTextColor(Color.GREEN);
                break;
            case UI_STATE_ERROR:
                tfCardIconImageView.setImageResource(android.R.drawable.stat_notify_sdcard_usb);
                tfCardAvailableSizeTextView.setTextColor(Color.RED);
                break;
            case UI_STATE_ELSE:
                tfCardIconImageView.setImageResource(android.R.drawable.stat_notify_sdcard_prepare);
                tfCardAvailableSizeTextView.setTextColor(Color.YELLOW);
        }
    }

    public void setTfCardSizeText(CharSequence text) {
        tfCardAvailableSizeTextView.setText(text);
    }

    public void setGpsUiIsShow(boolean isShow) {

        LinearLayout linearLayout = (LinearLayout) leftUpUiLayout.getChildAt(0);

        if (isShow) {
            if (gpsIconImageView.getParent() == null)
                linearLayout.addView(gpsIconImageView, ICON_WIDTH, ICON_HEIGHT);
            if (gpsLongitudeTextView.getParent() == null) {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, ICON_HEIGHT);
                linearLayout.addView(gpsLongitudeTextView, layoutParams);
            }
            if (gpsLatitudeTextView.getParent() == null) {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, ICON_HEIGHT);
                linearLayout.addView(gpsLatitudeTextView, layoutParams);
            }
        } else {
            linearLayout.removeAllViews();
        }
    }

    public void setGpsLongitudeTextColor(int color) {
        gpsLongitudeTextView.setTextColor(color);
    }

    public void setGpsLatitudeTextColor(int color) {
        gpsLatitudeTextView.setTextColor(color);
    }

    public void setGpsLongitudeText(CharSequence text) {
        gpsLongitudeTextView.setText(text);
    }

    public void setGpsLatitudeText(CharSequence text) {
        gpsLatitudeTextView.setText(text);
    }

    public TextView getDistanceTextView() {
        return distanceTextView;
    }
}