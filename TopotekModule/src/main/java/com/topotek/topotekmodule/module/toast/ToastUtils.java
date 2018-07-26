package com.topotek.topotekmodule.module.toast;

import android.content.Context;
import android.os.Environment;
import android.os.Looper;

import com.topotek.module.android.thread.MainThreadHandler;
import com.topotek.module.android.toast.ToastManager;

//ok
public class ToastUtils {

    /**
     * 不检查参数
     *
     * @param context 用于toast
     * @param code    CameraModule.initCamera
     * @return true: 1
     */
    public static boolean initCameraToast(Context context, int code) {
        switch (code) {
            case 1:
                return true;
            case -1:
                toast(context, "打开相机出错");
                return false;
            case -2:
                toast(context, "打开相机失败");
                return false;
            case -3:
                toast(context, "相机异常");
                return false;
            default:
                return false;
        }
    }

    /**
     * 不检查参数
     *
     * @param context     用于toast
     * @param tfCardState Environment.getExternalStorageState
     * @return true: Environment.MEDIA_MOUNTED
     */
    public static boolean tfCardStateToast(Context context, String tfCardState) {
        switch (tfCardState) {
            case Environment.MEDIA_MOUNTED://TF卡安装好的
                toast(context, "TF卡状态正常");
                return true;
            case Environment.MEDIA_CHECKING://正在检测TF卡
                toast(context, "TF卡未准备好");
                return false;
            case Environment.MEDIA_UNMOUNTED://TF卡未挂载
                toast(context, "TF卡未挂载");
                return false;
            case Environment.MEDIA_UNMOUNTABLE://TF卡不能挂载
                toast(context, "TF卡无法被挂载");
                return false;
            case Environment.MEDIA_NOFS://不支持该TF卡的文件系统或TF卡无文件系统
                toast(context, "TF卡没有文件系统或不支持");
                return false;
            case Environment.MEDIA_UNKNOWN://不知道的状态
                toast(context, "未知的TF卡状态");
                return false;
            default://其他
                toast(context, "非正常的TF卡状态");
                return false;
        }
    }

    /**
     * 不检查参数
     * <p>
     * 若是子线程调用，自动发送到主线程执行
     */
    public static void toast(final Context context, final int duration, final int backgroundColor, final int textColor, final CharSequence text) {
        if (Looper.myLooper() == Looper.getMainLooper())
            ToastManager.toast(context, duration, backgroundColor, textColor, text);
        else {
            MainThreadHandler.post(new Runnable() {
                @Override
                public void run() {
                    ToastManager.toast(context, duration, backgroundColor, textColor, text);
                }
            });
        }
    }

    /**
     * 不检查参数
     * <p>
     * 若是子线程调用，自动发送到主线程执行
     */
    public static void toast(Context context, CharSequence text) {
        toast(context, ToastManager.DefaultDuration, ToastManager.DefaultBackgroundColor, ToastManager.DefaultTextColor, text);
    }
}
