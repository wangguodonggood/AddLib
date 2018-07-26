package com.topotek.module.android.debug;

import android.app.Activity;
import android.graphics.Color;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.TextView;

public class Debugger {

    public static boolean isDebug = false;

    private static Activity mActivity;
    private static TextView textView_ShowView;
    private static TextView textView_AddShowView;

    private static boolean isInit = false;

    public static Activity getActivity() {
        return mActivity;
    }

    public static void init(@NonNull Activity activity, @Nullable View.OnClickListener onClickListener) {

        if (!isDebug)
            return;

        mActivity = activity;

        final FrameLayout frameLayout_DebugView = new FrameLayout(activity);

        textView_AddShowView = new TextView(activity);
        textView_AddShowView.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
        textView_AddShowView.setTextColor(Color.RED);
        textView_AddShowView.setText("Debugger");
        frameLayout_DebugView.addView(textView_AddShowView);

        textView_ShowView = new TextView(activity);
        textView_ShowView.setTextColor(Color.RED);
        FrameLayout.LayoutParams showViewLayoutParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.RIGHT);
        frameLayout_DebugView.addView(textView_ShowView, showViewLayoutParams);

        Chronometer chronometer = new Chronometer(activity);
        chronometer.setBackgroundColor(Color.parseColor("#66000000"));
        chronometer.setGravity(Gravity.RIGHT);
        chronometer.setFormat("Debugger\n%s");
        chronometer.setBase(0);
        chronometer.start();
        chronometer.setOnClickListener(onClickListener);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        frameLayout_DebugView.addView(chronometer, layoutParams);

        Window window = activity.getWindow();
        final ViewGroup decorView = (ViewGroup) window.getDecorView();

        if (Looper.myLooper() == Looper.getMainLooper()) {

            decorView.addView(frameLayout_DebugView);

            isInit = true;
        } else {

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    decorView.addView(frameLayout_DebugView);

                    isInit = true;
                }
            });
        }
    }

    public static void show(final CharSequence text) {

        if (!isDebug || !isInit)
            return;

        if (Looper.myLooper() == Looper.getMainLooper())
            textView_ShowView.setText(text);
        else
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    textView_ShowView.setText(text);
                }
            });
    }

    public static void addShow(final CharSequence text) {

        if (!isDebug || !isInit || text == null)
            return;

        if (Looper.myLooper() == Looper.getMainLooper()) {

            CharSequence oldText = textView_AddShowView.getText();
            StringBuilder stringBuilder = new StringBuilder(oldText).append(text);
            textView_AddShowView.setText(stringBuilder);
        } else {

            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    addShow(text);
                }
            });
        }
    }
}
