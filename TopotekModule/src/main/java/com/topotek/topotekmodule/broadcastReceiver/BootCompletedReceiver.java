package com.topotek.topotekmodule.broadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;

import com.topotek.libs.libs0.project.language.Strings;
import com.topotek.module.java.file.SimpleReadFileUtils;
import com.topotek.module.java.file.SimpleWriteFileUtils;
import com.topotek.module.java.file.SpecialReadFileUtils;
import com.topotek.module.java.threadExecutor.ThreadPool;
import com.topotek.module.project.storageDevice.TfCardHelper;
import com.topotek.topotekmodule.module.toast.ToastUtils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class BootCompletedReceiver extends BroadcastReceiver {

    public static int b_c_n = -10;

    @Override
    public void onReceive(final Context context, Intent intent) {
        ThreadPool.execute(createRunnable(context));
    }

    private static Runnable createRunnable(final Context context) {
        return new Runnable() {
            @Override
            public void run() {

                File tfCardDirectory = TfCardHelper.getTfCardDirectory();
                if (tfCardDirectory == null) {
                    ToastUtils.toast(context.getApplicationContext(), "NO TF Card");
                    return;
                }

                String tfCardState = Environment.getExternalStorageState(tfCardDirectory);
                switch (tfCardState) {
                    case Environment.MEDIA_MOUNTED:
                        int b_c_n = 0;
                        File file = new File(tfCardDirectory, ".B_C_N");
                        if (file.exists()) {
                            if(file.isDirectory()){
                                BootCompletedReceiver.b_c_n = -9;
                                break;
                            }
                            b_c_n = SpecialReadFileUtils.readPositiveInteger(file, 10);
                            if(b_c_n < 0)
                                b_c_n = 0;
                        }
                        BootCompletedReceiver.b_c_n = ++b_c_n;
                        SimpleWriteFileUtils.writeFile(file, String.valueOf(b_c_n));
                        break;
                    case Environment.MEDIA_CHECKING://正在检测TF卡(TF卡未准备好)
                        ThreadPool.schedule(this, 2, TimeUnit.SECONDS);
                        break;
                    default:
                        ToastUtils.tfCardStateToast(context.getApplicationContext(), tfCardState);
                        break;
                }
            }
        };
    }
}
