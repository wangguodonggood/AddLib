package com.topotek.topotekmodule.task;

import android.os.Environment;
import android.os.StatFs;

import com.topotek.libs.libs0.project.language.Strings;
import com.topotek.module.android.thread.MainThreadHandler;
import com.topotek.module.java.threadExecutor.ThreadPool;
import com.topotek.module.project.storageDevice.TfCardHelper;
import com.topotek.module.project.uiView.UiLayout;
import com.topotek.topotekmodule.commandExecuter.CommandExecuter;
import com.topotek.topotekmodule.uiModule.UiModule;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class TfCardCheckTask {

    private static File tfCardDirectory;

    public static void exec() {

        setTfCardUiIsShow(true);

        tfCardDirectory = TfCardHelper.getTfCardDirectory();
        if (tfCardDirectory == null) {
            setTfCardErrorUi(Strings.未检测到());
            return;
        }

        new Runnable() {
            @Override
            public void run() {
                switch (Environment.getExternalStorageState(tfCardDirectory)) {
                    case Environment.MEDIA_MOUNTED://TF卡安装好的
                        setTfCardUiState(UiLayout.UI_STATE_OK);
                        break;
                    case Environment.MEDIA_CHECKING://正在检测TF卡
                        setTfCardUi(UiLayout.UI_STATE_ELSE, Strings.正在检测());
                        ThreadPool.schedule(this, 2, TimeUnit.SECONDS);
                        return;
                    case Environment.MEDIA_UNMOUNTED://TF卡未挂载
                        setTfCardErrorUi(Strings.未挂载());
                        return;
                    case Environment.MEDIA_UNMOUNTABLE://TF卡不能挂载
                        setTfCardErrorUi(Strings.无法挂载());
                        return;
                    case Environment.MEDIA_NOFS://不支持该TF卡的文件系统或TF卡无文件系统
                        setTfCardErrorUi(Strings.没有文件系统或不支持());
                        return;
                    case Environment.MEDIA_UNKNOWN://不知道的状态
                        setTfCardErrorUi(Strings.未知状态());
                        return;
                    default:
                            setTfCardErrorUi(Strings.异常());
                        return;
                }

                ThreadPool.execute(newRunnable());
            }
        }.run();
    }

    private static Runnable newRunnable() {
        return new Runnable() {

            private String tfCardDirectoryAbsolutePath = tfCardDirectory.getAbsolutePath();

            private String tfCardSizeText;
            private Runnable mRunnable = new Runnable() {
                @Override
                public void run() {
                    UiModule.uiLayout.setTfCardSizeText(tfCardSizeText);
                }
            };

            private void showTfCardSizeText(String tfCardSizeText) {
                this.tfCardSizeText = tfCardSizeText;
                MainThreadHandler.post(mRunnable);
            }

            @Override
            public void run() {

                StatFs statFs = new StatFs(tfCardDirectoryAbsolutePath);
                long availableBytes = statFs.getAvailableBytes();
                long availableMB = availableBytes / 1048576;//    1048576 = 1024 * 1024
                long gb = availableMB / 1024;
                long mb = availableMB % 1024;
                final String g = gb > 0 ? String.valueOf(gb) + 'G' : "";
                final String m = mb > 0 ? String.valueOf(mb) + 'M' : "";

                long byteOneS = 1;//---------------------------------------------
                if (CommandExecuter.mainCameraIsRecord)
                    byteOneS += 2621440;//2.5m
                if (CommandExecuter.subsidiaryCameraIsRecord)
                    byteOneS += 524288;//3.5m  3670016    //0.5m  524288
                long allM = availableBytes / byteOneS / 60;
                final long h = allM / 60;
                final long M = allM % 60;//---------------------------------------------

                showTfCardSizeText(g + m + "≈" + h + ":" + M);

                ThreadPool.schedule(this, 2, TimeUnit.SECONDS);
            }
        };
    }

    private static void setTfCardUiIsShow(final boolean isShow) {
        MainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                UiModule.uiLayout.setTfCardUiIsShow(isShow);
            }
        });
    }

    private static void setTfCardUiState(final int uiState) {
        MainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                UiModule.uiLayout.setTfCardUiState(uiState);
            }
        });
    }

    private static void setTfCardUi(final int uiState, final String tfCardLog) {
        MainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                UiModule.uiLayout.setTfCardUiState(uiState);
                UiModule.uiLayout.setTfCardSizeText(tfCardLog);
            }
        });
    }

    private static void setTfCardErrorUi(String tfCardErrorLog) {
        setTfCardUi(UiLayout.UI_STATE_ERROR, tfCardErrorLog);
    }
}
