package com.topotek.topotekmodule.SimplifyCallback;

import android.content.Context;
import android.os.Environment;

import com.topotek.libs.libs0.project.language.Strings;
import com.topotek.libs.libs1.android.view.cameraView.CameraView;
import com.topotek.module.android.debug.Debugger;
import com.topotek.module.android.thread.MainThreadHandler;
import com.topotek.module.java.file.FileNameUtils;
import com.topotek.module.java.file.SpecialReadFileUtils;
import com.topotek.module.project.file.FileNameTools;
import com.topotek.module.project.storageDevice.TfCardHelper;
import com.topotek.topotekmodule.broadcastReceiver.BootCompletedReceiver;
import com.topotek.topotekmodule.cameraManage.CameraManager;
import com.topotek.topotekmodule.module.toast.ToastUtils;

import java.io.File;

public class MyCameraManagerCaptureCallback implements CameraManager.CaptureCallback {

    private Context context;
    private CameraView cameraUiView;

    public MyCameraManagerCaptureCallback(Context context, CameraView cameraUiView) {
        this.context = context;
        this.cameraUiView = cameraUiView;
    }

    @Override
    public void onPictureStart(int cameraFacing) {
        MainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                cameraUiView.captureStartUI();
            }
        });
    }

    @Override
    public void onPictureFinish(int cameraFacing) {
        MainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                cameraUiView.captureFinishUI();
            }
        });
    }

    @Override
    public void onRecordStart(int cameraFacing) {
        MainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                cameraUiView.recordStartUI();
            }
        });
    }

    @Override
    public void onRecordFinish(int cameraFacing) {
        MainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                cameraUiView.recordFinishUI();
            }
        });
    }

    public static boolean cameraIsAlwaysHaveTfCard = false;

    @Override
    public boolean checkTfCard(int cameraFacing) {

        File tfCardDirectory = TfCardHelper.getTfCardDirectory();
        if(tfCardDirectory == null){

            ToastUtils.toast(context.getApplicationContext(), Strings.未检测到TF卡());

            if(cameraIsAlwaysHaveTfCard)
                return true;

            return false;
        }

        String tfCardState = Environment.getExternalStorageState(tfCardDirectory);
        if(Environment.MEDIA_MOUNTED.equals(tfCardState))
            return true;
        else
            return ToastUtils.tfCardStateToast(context.getApplicationContext(), tfCardState);
    }

    @Override
    public File getOutputFile(int cameraFacing, String file_Extension) {

        File externalStorageDirectory = TfCardHelper.getTfCardDirectory();
        if (externalStorageDirectory == null) {
            externalStorageDirectory = Environment.getExternalStorageDirectory();
        }

        File fileStorageDirectory = new File(externalStorageDirectory, "DCIM");
        if (fileStorageDirectory.exists()) {
            if(!fileStorageDirectory.isDirectory())
                fileStorageDirectory = externalStorageDirectory;
        }else {
            if (!fileStorageDirectory.mkdirs())
                fileStorageDirectory = externalStorageDirectory;
        }

        String fileStoragePath = fileStorageDirectory.getAbsolutePath();

        if(BootCompletedReceiver.b_c_n < -9){
            File fileB_C_N = new File(externalStorageDirectory, ".B_C_N");
            if(fileB_C_N.exists()) {
                if(fileB_C_N.isDirectory())
                    BootCompletedReceiver.b_c_n = -9;
                else {
                    BootCompletedReceiver.b_c_n = SpecialReadFileUtils.readPositiveInteger(fileB_C_N, 10);
                }
            }else
                BootCompletedReceiver.b_c_n = -8;
        }

        String elapsedRealtime = FileNameTools.elapsedRealtimeFormat();

        String head = fileStoragePath + "/" + BootCompletedReceiver.b_c_n + "_" + elapsedRealtime + "_" + (cameraFacing + 1);

        File file = FileNameUtils.noTautonomy(head, file_Extension, Integer.MAX_VALUE);

        if(file != null)
            Debugger.addShow("    " + file.getAbsolutePath());

        return file;
    }
}
