package com.topotek.modules.modules0.project.imageBlendModule;

import android.content.Context;

import com.topotek.libs.libs0.project.imageBlendDataHandle.ImageBlendData;
import com.topotek.libs.libs1.project.parameterStorage.ParameterStorage;

import java.util.ArrayList;

public class ImageBlendModule {

    private static final String ParameterStorageName = "ImageBlend";
    private static final String ZoomParameterStorageKeyHead = "zoom";
    private static final String PreviewView1SizeScaleParameterStorageKeyHead = "previewView1SizeScale";
    private static final String ImageBlendDatasSizeParameterStorageKey = "imageBlendDatasSize";

    public static ArrayList<ImageBlendData> imageBlendDatas;
    public static boolean isBlend = false;
    public static float previewView0Alpha = 0.4F;

    public static ArrayList<ImageBlendData> getImageBlendDatas(Context context) {
        ParameterStorage parameterStorage = new ParameterStorage(context, ParameterStorageName);
        ArrayList<ImageBlendData> imageBlendDatas = new ArrayList<>();
        int imageBlendDatasSize = parameterStorage.getInt(ImageBlendDatasSizeParameterStorageKey, 0);
        for (int index = 0; index < imageBlendDatasSize; ++index) {
            float zoom = parameterStorage.getFloat(ZoomParameterStorageKeyHead + index, 0);
            float previewView1SizeScale = parameterStorage.getFloat(PreviewView1SizeScaleParameterStorageKeyHead + index, 0);
            ImageBlendData imageBlendData = new ImageBlendData(zoom, previewView1SizeScale);
            imageBlendDatas.add(imageBlendData);
        }
        return imageBlendDatas;
    }

    public static void saveImageBlendDatas(Context context, ArrayList<ImageBlendData> imageBlendDatas) {
        ParameterStorage parameterStorage = new ParameterStorage(context, ParameterStorageName);
        int imageBlendDatasSize = imageBlendDatas.size();
        for (int index = 0; index < imageBlendDatasSize; ++index) {
            ImageBlendData imageBlendData = imageBlendDatas.get(index);
            parameterStorage.putFloat(ZoomParameterStorageKeyHead + index, imageBlendData.zoom);
            parameterStorage.putFloat(PreviewView1SizeScaleParameterStorageKeyHead + index, imageBlendData.previewView1SizeScale);
        }
        parameterStorage.putInt(ImageBlendDatasSizeParameterStorageKey, imageBlendDatasSize);
    }
}
