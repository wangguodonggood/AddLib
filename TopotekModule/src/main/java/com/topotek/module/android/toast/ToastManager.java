package com.topotek.module.android.toast;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

//ok
public class ToastManager {

    public static final int DefaultDuration = Toast.LENGTH_LONG;
    public static final int DefaultBackgroundColor = Color.parseColor("#99ff0000");
    public static final int DefaultTextColor = Color.WHITE;

    private static Toast mToast;

    /**
     * 不检查参数
     * <p>
     * 设计用于主线程调用
     */
    public static void toast(Context context, int duration, int backgroundColor, int textColor, CharSequence text) {

        TextView textView;

        if (mToast == null) {
            //ToastView
            textView = new TextView(context);
            textView.setPadding(30, 30, 30, 30);
            textView.setTextSize(20F);
            textView.setGravity(Gravity.CENTER);
            //Toast
            mToast = new Toast(context);
            mToast.setGravity(Gravity.RIGHT | Gravity.BOTTOM, 0, 0);
            mToast.setView(textView);
        } else
            textView = (TextView) mToast.getView();

        //setToast
        mToast.setDuration(duration);
        //setToastView
        textView.setBackgroundColor(backgroundColor);
        textView.setTextColor(textColor);
        textView.setText(text);

        mToast.show();
    }

    /**
     * 不检查参数
     * <p>
     * 设计用于主线程调用
     */
    public static void toast(Context context, CharSequence text) {
        toast(context, DefaultDuration, DefaultBackgroundColor, DefaultTextColor, text);
    }
}
