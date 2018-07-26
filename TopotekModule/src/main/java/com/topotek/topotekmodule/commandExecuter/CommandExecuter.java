package com.topotek.topotekmodule.commandExecuter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Camera;
import android.os.StatFs;
import android.widget.Toast;

import com.topotek.libs.libs0.android.view.doublePreviewLayout.DynamicDoublePreviewLayout;
import com.topotek.libs.libs0.project.imageBlendDataHandle.ImageBlendData;
import com.topotek.libs.libs0.project.imageBlendDataHandle.ImageBlendDataHandler;
import com.topotek.module.android.debug.Debugger;
import com.topotek.module.android.system.Setting;
import com.topotek.module.android.thread.MainThreadHandler;
import com.topotek.module.java.threadExecutor.FunnelExecutor;
import com.topotek.module.project.commandAnalyzer.CommandAnalyzer;
import com.topotek.module.project.storageDevice.TfCardHelper;
import com.topotek.modules.modules0.project.cameraZoom.CameraZoomModule;
import com.topotek.modules.modules0.project.imageBlendModule.ImageBlendModule;
import com.topotek.modules.modules0.project.parameterStorageModule.ParameterStorageModule;
import com.topotek.topotekmodule.cameraManage.CameraManager;
import com.topotek.topotekmodule.cameraModule.CameraModule;
import com.topotek.topotekmodule.cameraView.AutoPreviewView;
import com.topotek.topotekmodule.menu.MenuModule;
import com.topotek.topotekmodule.module.toast.ToastUtils;
import com.topotek.topotekmodule.task.GpsManageTask;
import com.topotek.topotekmodule.uiModule.MenuUtils;
import com.topotek.topotekmodule.uiModule.UiModule;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
//具体实现命令的类   CommandExecuter接口实现类    根据功能要添加新函数
public class CommandExecuter implements CommandAnalyzer.CommandExecuter {
    private static final char NEXT = 'A';
    private static final char PREVIOUS = 'B';
    private static final char NOT = 'N';
    private static final char TRUE = '1';
    private static final char FALSE = '0';
    private Context mContext;
    private DynamicDoublePreviewLayout mDynamicDoublePreviewLayout;
    public CommandExecuter(Context context, DynamicDoublePreviewLayout dynamicDoublePreviewLayout){
        mContext = context;
        mDynamicDoublePreviewLayout = dynamicDoublePreviewLayout;
    }
    private void sendCommandBroadcast(String s) {
        mContext.sendBroadcast(new Intent("com.topotek.service.write").putExtra("command", s));
    }
    @Override
    public void rSDC() {
        File tfCardDirectory = TfCardHelper.getTfCardDirectory();
        if(tfCardDirectory == null){
            ToastUtils.toast(mContext.getApplicationContext(), "未检测到TF卡");
            return;
        }
        StatFs statFs = new StatFs(tfCardDirectory.getAbsolutePath());
        long tfCardStorageSize = statFs.getAvailableBytes();
        long tfCardStorageSize_M = tfCardStorageSize / (1024 * 1024);
        String hexTfCardStorageSize_M = Long.toHexString(tfCardStorageSize_M);
        for (int length = hexTfCardStorageSize_M.length(); length < 4; length++) {
            hexTfCardStorageSize_M = "0" + hexTfCardStorageSize_M;
        }
        if (hexTfCardStorageSize_M.length() > 4)
            hexTfCardStorageSize_M = "ffff";

        sendCommandBroadcast("#tpDU4rSDC" + hexTfCardStorageSize_M);
    }
    @Override
    public void rPIP() {
        sendCommandBroadcast("#TPDU2rPIP0" + mDynamicDoublePreviewLayout.getCurrentPreviewLayoutMode());
    }
    @Override
    public void rREC() {
        StringBuilder stringBuilder = new StringBuilder("#TPDU2rREC");
        for (int facing = 0; facing <= 1; facing++) {
            switch (facing){
                case 0:
                    int state = CameraModule.cameraManager.getState();
                    char c = state == CameraManager.STATE_PREVIEWING ? '0' : '1';
                    stringBuilder.append(c);
                    break;
                case 1:
                    stringBuilder.append('0');
                    break;
            }
        }
        sendCommandBroadcast(stringBuilder.toString());
    }
    @Override
    public void rAWB() {
        int whiteBalanceIndex = CameraModule.cameraManager.getWhiteBalanceIndex();
        sendCommandBroadcast("#TPDU2rAWB0" + whiteBalanceIndex);
    }
    @Override
    public void rEVS() {
        int exposureCompensation = CameraModule.cameraManager.getExposureCompensation();
        StringBuilder stringBuilder = new StringBuilder("#TPDU2rEVS");
        if (exposureCompensation < 0) {
            stringBuilder.append('1');
            stringBuilder.append(-exposureCompensation);
        } else {
            stringBuilder.append('0');
            stringBuilder.append(exposureCompensation);
        }
        sendCommandBroadcast(stringBuilder.toString());
    }
    @Override
    public void rISO() {
        int isoIndex = CameraModule.cameraManager.getIsoIndex();
        sendCommandBroadcast("#TPDU2rISO0" + isoIndex);
    }
    @Override
    public void rPIC() {
        int pictureSizeIndex0 = CameraModule.cameraManager.getPictureSizeIndex();
        int pictureSizeIndex1 = 0;//======================================================
        sendCommandBroadcast("#TPDU2rPIC" + pictureSizeIndex0 + pictureSizeIndex1);
    }
    @Override
    public void rVID() {
        int videoSizeIndex0 = CameraModule.cameraManager.getVideoSizeIndex();
        int videoSizeIndex1 = 0;//=======================================================
        sendCommandBroadcast("#TPDU2rVID" + videoSizeIndex0 + videoSizeIndex1);
    }
    @Override
    public void wPIP(final char parameter) {
        if(ImageBlendModule.isBlend){
            float alpha = UiModule.previewView0.getAlpha();
            if(alpha <= 0F){
                UiModule.previewView0.setAlpha(ImageBlendModule.previewView0Alpha);
            }else if(alpha >= 1F){
                UiModule.previewView0.setAlpha(0F);
            }else {
                UiModule.previewView0.setAlpha(1F);
            }
            return;
        }
        MainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                switch (parameter) {
                    case NEXT:
                        mDynamicDoublePreviewLayout.nextPreviewLayoutMode();
                        break;
                    case PREVIOUS:
                        mDynamicDoublePreviewLayout.previousPreviewLayoutMode();
                        break;
                    default:
                        mDynamicDoublePreviewLayout.setPreviewLayoutMode(parameter - '0');
                }
            }
        });
    }
    @Override
    public void wAWB(char[] parameters) {
        switch (parameters[1]) {
            case NEXT:
                CameraModule.cameraManager.changeWhiteBalance(1);
                break;
            case PREVIOUS:
                CameraModule.cameraManager.changeWhiteBalance(-1);
                break;
            default:
                CameraModule.cameraManager.setWhiteBalance(parameters[1] - '0');
        }
    }
    @Override
    public void wISO(char[] parameters) {
        switch (parameters[1]) {
            case NEXT:
                CameraModule.cameraManager.changeISO(1);
                break;
            case PREVIOUS:
                CameraModule.cameraManager.changeISO(-1);
                break;
            default:
                CameraModule.cameraManager.setISO(parameters[1] - '0');
        }
    }
    @Override
    public void wEVS(char[] parameters) {
        switch (parameters[1]) {
            case NEXT:
                CameraModule.cameraManager.changeExposureCompensation(1);
                break;
            case PREVIOUS:
                CameraModule.cameraManager.changeExposureCompensation(-1);
                break;
            default:
                CameraModule.cameraManager.setExposureCompensation(parameters[1] - '0');
        }
    }
    @Override
    public void wPIC(char[] parameters) {
        switch (parameters[Camera.CameraInfo.CAMERA_FACING_BACK]) {
            case NEXT:
                CameraModule.cameraManager.changePictureSize(1);
                break;
            case PREVIOUS:
                CameraModule.cameraManager.changePictureSize(-1);
                break;
            case NOT:
                break;
            default:
                CameraModule.cameraManager.setPictureSize(parameters[Camera.CameraInfo.CAMERA_FACING_BACK] - '0');
        }
    }
    @Override
    public void wVID(char[] parameters) {
        switch (parameters[Camera.CameraInfo.CAMERA_FACING_BACK]) {
            case NEXT:
                CameraModule.cameraManager.changeVideoSize(1);
                break;
            case PREVIOUS:
                CameraModule.cameraManager.changeVideoSize(-1);
                break;
            case NOT:
                break;
            default:
                CameraModule.cameraManager.
                        setVideoSize(parameters[Camera.CameraInfo.CAMERA_FACING_BACK] - '0');
        }
    }
    @Override
    public void wCAP(char[] parameters) {
        if(mainCameraIsCapture){
            switch (parameters[Camera.CameraInfo.CAMERA_FACING_BACK]) {
                case TRUE:
                    FunnelExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            CameraModule.cameraManager.picture();
                        }
                    });
                    break;
            }
        }
        if(subsidiaryCameraIsCapture){
            switch (parameters[Camera.CameraInfo.CAMERA_FACING_FRONT]){
                case TRUE:
                    CameraModule.usbCameraCapture();
                    break;
            }
        }
    }
    /*Executor使用线程池来管理线程，可以重复利用已经创建出来的线程而不是每次都必须新创建线程，节省了一部分的开销。
线程池也可以很方便的管理线程的大小和当前在执行的线程数量。
可以认为Executor是帮助管理Thread的封装好的工具。*/
    @Override
    public void wREC(char[] parameters) {
        if(mainCameraIsRecord){
            switch (parameters[Camera.CameraInfo.CAMERA_FACING_BACK]) {
                case NEXT:
                    FunnelExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            CameraModule.cameraManager.record();
                        }
                    });
                    break;
            }
        }
        if(subsidiaryCameraIsRecord){
            switch (parameters[Camera.CameraInfo.CAMERA_FACING_FRONT]){
                case NEXT:
                    CameraModule.usbCameraRecord();
                    break;
            }
        }
    }
    @Override
    public void wDIS(char[] parameters) {
        switch (parameters[1]){
            case '0':
                CameraModule.cameraManager.setDisplayInverseOrientation(false);
                AutoPreviewView.isRorate = false;
                break;
            case '1':
                CameraModule.cameraManager.setDisplayInverseOrientation(true);
                AutoPreviewView.isRorate = true;
                break;
        }
    }
    @Override
    public void wZMP(final String parameter) {
        final String string;
        if(parameter.charAt(0) == '0')
            string = String.valueOf(parameter.charAt(1));
        else
            string = parameter.substring(0, 2);

        ToastUtils.toast(mContext.getApplicationContext(), Toast.LENGTH_SHORT, Color.TRANSPARENT, Color.GREEN, string + '.' + parameter.charAt(2) + '×');


        if(parameter.length() < 4)
            return;

        String hexZoom = parameter.substring(3);
        try {
            CameraZoomModule.currentZoom = Integer.parseInt(hexZoom, 16);
        }catch (Exception e){
            e.printStackTrace();
            return;
        }
        Debugger.addShow("  " + CameraZoomModule.currentZoom);
        CameraZoomModule.isActivate = true;
        if(ImageBlendModule.isBlend){
            final ImageBlendData imageBlendData = new ImageBlendData(CameraZoomModule.currentZoom);
            ImageBlendDataHandler.getImageBlendData(ImageBlendModule.imageBlendDatas, imageBlendData);
            MainThreadHandler.post(new Runnable() {
                @Override
                public void run() {
                    UiModule.cameraView1.setPreviewViewSizeScale(imageBlendData.previewView1SizeScale);
                }
            });
        }
    }

    @Override
    public void wTIM(String parameter) {

        Setting.initSystemTimeSetting(mContext);
        Setting.setSystemTimeZone(mContext, 28800000);//北京时间  东八区 +08:00    1000 * 60 * 60 * 8 = 28800000(毫秒)

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String[] availableIDs = TimeZone.getAvailableIDs(0);
        if(availableIDs != null && availableIDs.length > 0){
            TimeZone timeZone = TimeZone.getTimeZone(availableIDs[0]);
            simpleDateFormat.setTimeZone(timeZone);
        }
        String time = "20" + parameter.substring(9) + parameter.substring(0, 6);
        Date date = null;
        try {
            date = simpleDateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(date != null){
            long millis = date.getTime();
            Setting.setSystemTime(mContext, millis);
            setTime_millis = millis;
        }
    }

    public static long setTime_millis = 0;

    @Override
    public void wLAT(String parameter) {
        GpsManageTask.updataLatitude(parameter);

        String angle = parameter.substring(1, parameter.length() - 8);
        String minute = parameter.substring(parameter.length() - 8);
        double f = Double.valueOf(minute);
        int i = Integer.valueOf(angle);
        double latitude = i + f / 60;
        latitude = (parameter.charAt(0) == 'W' ? -1 : 1) * latitude;
        CameraModule.cameraManager.setGpsLatitude(latitude);
    }

    @Override
    public void wLON(String parameter) {
        GpsManageTask.updataLongitude(parameter);

        String angle = parameter.substring(1, parameter.length() - 8);
        String minute = parameter.substring(parameter.length() - 8);
        double f = Double.valueOf(minute);
        int i = Integer.valueOf(angle);
        double longitude = i + f / 60;
        longitude = (parameter.charAt(0) == 'S' ? -1 : 1) * longitude;
        CameraModule.cameraManager.setGpsLongitude(longitude);
    }

    @Override
    public void wLRF(final String parameter) {
        int parameterLength = parameter.length();
        int headLength = 0;
        for(int index = 0; index < parameterLength; ++index){
            char c = parameter.charAt(index);
            if(c == '.')
                break;
            ++headLength;
        }
        if(headLength < 3)
            return;
        int z = headLength - 1;
        int index = 0;
        while (index < z){
            char c = parameter.charAt(index);
            if(c != '0')
                break;

            ++index;
        }
        final String substring = parameter.substring(index);
        MainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                UiModule.uiLayout.getDistanceTextView().setText(substring + "m");
            }
        });
    }

    @Override
    public void wTRC() {
        mContext .sendBroadcast(new Intent("com.topotek.service.data").putExtra("string", ":wKey7"));
    }

    public static boolean mainCameraIsCapture = ParameterStorageModule.parameterStorage.getBoolean("mainCameraIsCapture", true);
    public static boolean subsidiaryCameraIsCapture = ParameterStorageModule.parameterStorage.getBoolean("subsidiaryCameraIsCapture", false);
    public static boolean mainCameraIsRecord = ParameterStorageModule.parameterStorage.getBoolean("mainCameraIsRecord", true);
    public static boolean subsidiaryCameraIsRecord = ParameterStorageModule.parameterStorage.getBoolean("subsidiaryCameraIsRecord", false);

    @Override
    public void wRec(char parameter) {
        switch (parameter) {
            case 'A':
                wREC(new char[]{mainCameraIsRecord ? 'A' : 'N', subsidiaryCameraIsRecord ? 'A' : 'N'});
                break;
            case 'B':
                wCAP(new char[]{mainCameraIsCapture ? '1' : 'N', subsidiaryCameraIsCapture ? '1' : 'N'});
                break;
            case 'C':
                wPIP('A');
                break;
        }
    }

    @Override
    public void wKey(final char parameter) {
        MainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                switch (parameter){
                    case '1':
                        MenuUtils.menuUp(UiModule.menu);
                        break;
                    case '2':
                        MenuUtils.menuDown(UiModule.menu);
                        break;
                    case '3':
                        MenuUtils.menuLeft(UiModule.menu);
                        break;
                    case '4':
                        MenuUtils.menuRight(UiModule.menu);
                        break;
                    case '5':
                        MenuUtils.menuOk(UiModule.menu);
                        break;
                    case '6':
                        MenuUtils.menu(UiModule.menu, UiModule.uiLayout);
                        break;
                    case '0':
                        if(UiModule.menu.getParent() != null)
                            UiModule.uiLayout.removeView(UiModule.menu);

                        UiModule.menu = MenuModule.createMenuModule(mContext);
                        MenuUtils.menu(UiModule.menu, UiModule.uiLayout);
                        break;
                }
            }
        });
    }

    @Override
    public void wIMG(char[] parameter) {

    }

    @Override
    public void wFUS(final char parameter) {
        if(!CameraZoomModule.isActivate)
            return;

        if(ImageBlendModule.imageBlendDatas == null)
            ImageBlendModule.imageBlendDatas = ImageBlendModule.getImageBlendDatas(mContext);

        MainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                switch (parameter){
                    case FALSE:
                        ImageBlendModule.isBlend = false;
                        int currentPreviewLayoutMode = mDynamicDoublePreviewLayout.getCurrentPreviewLayoutMode();
                        mDynamicDoublePreviewLayout.setPreviewLayoutMode(currentPreviewLayoutMode);
                        UiModule.cameraView1.setPreviewViewSizeScale(1);
                        UiModule.previewView0.setAlpha(1F);
                        break;
                    case TRUE:
                        ImageBlendModule.isBlend = true;
                        mDynamicDoublePreviewLayout.setPreviewLayoutMode_Superposition_10();
                        ImageBlendData imageBlendData = new ImageBlendData(CameraZoomModule.currentZoom);
                        ImageBlendDataHandler.getImageBlendData(ImageBlendModule.imageBlendDatas, imageBlendData);
                        UiModule.cameraView1.setPreviewViewSizeScale(imageBlendData.previewView1SizeScale);
                        UiModule.previewView0.setAlpha(ImageBlendModule.previewView0Alpha);
                        break;
                    case NEXT:
                        if(ImageBlendModule.isBlend){
                            int previewLayoutMode = mDynamicDoublePreviewLayout.getCurrentPreviewLayoutMode();
                            mDynamicDoublePreviewLayout.setPreviewLayoutMode(previewLayoutMode);
                            UiModule.cameraView1.setPreviewViewSizeScale(1);
                            UiModule.previewView0.setAlpha(1F);
                        }else{
                            mDynamicDoublePreviewLayout.setPreviewLayoutMode_Superposition_10();
                            ImageBlendData imageBlendData1 = new ImageBlendData(CameraZoomModule.currentZoom);
                            ImageBlendDataHandler.getImageBlendData(ImageBlendModule.imageBlendDatas, imageBlendData1);
                            UiModule.cameraView1.setPreviewViewSizeScale(imageBlendData1.previewView1SizeScale);
                            UiModule.previewView0.setAlpha(ImageBlendModule.previewView0Alpha);
                        }
                        ImageBlendModule.isBlend = !ImageBlendModule.isBlend;
                        break;
                }
            }
        });
    }
}
