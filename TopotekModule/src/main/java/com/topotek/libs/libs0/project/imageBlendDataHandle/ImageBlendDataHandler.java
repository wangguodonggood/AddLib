package com.topotek.libs.libs0.project.imageBlendDataHandle;

import android.support.annotation.NonNull;

import java.util.ArrayList;

public class ImageBlendDataHandler {

    /**
     * 将newImageBlendData插入到arrayList的正确位置
     * (arrayList中的元素按zoom从小到大排序)
     */
    public static void putImageBlendData(@NonNull ArrayList<ImageBlendData> arrayList,
                                         @NonNull ImageBlendData newImageBlendData) {

        int arrayListSize = arrayList.size();

        //arrayList没有数据，第一次加入
        if (arrayListSize == 0) {
            arrayList.add(newImageBlendData);
            return;
        }

        //将newImageBlendData插入正确位置(arrayList中的元素按zoom从小到大排序)
        //插入到第一个zoom比newImageBlendData大的之前
        for (int index = 0; index < arrayListSize; ++index) {
            ImageBlendData imageBlendData = arrayList.get(index);
            if (imageBlendData.zoom == newImageBlendData.zoom) {
                arrayList.set(index, newImageBlendData);
                return;
            } else if (imageBlendData.zoom > newImageBlendData.zoom) {
                arrayList.add(index, newImageBlendData);
                return;
            }
        }

        //运行到这是因为arrayList中没有zoom比newImageBlendData的大的元素
        //由于newImageBlendData的zoom最大，所以将它加到arrayList尾部
        arrayList.add(newImageBlendData);
    }

    /**
     * @param arrayList      至少要已有2个元素,最小值和最大值(zoom)
     * @param imageBlendData imageBlendData.zoom要赋值
     */
    public static void getImageBlendData(@NonNull ArrayList<ImageBlendData> arrayList, @NonNull ImageBlendData imageBlendData) {
        int arrayListSize = arrayList.size();
        ImageBlendData imageBlendData0 = null;
        for (int index = 0; index < arrayListSize; ++index) {
            ImageBlendData imageBlendData1 = arrayList.get(index);
            if (imageBlendData1.zoom == imageBlendData.zoom) {
                imageBlendData.previewView1SizeScale = imageBlendData1.previewView1SizeScale;
                return;
            } else if (imageBlendData1.zoom > imageBlendData.zoom) {
                if (imageBlendData0 != null)
                    getImageBlendData(imageBlendData0, imageBlendData1, imageBlendData);
                return;
            }
            imageBlendData0 = imageBlendData1;
        }
    }

    /**
     * 所有previewView1SizeScale大于0;
     *
     * @param imageBlendData0 成员变量都要有正确值
     * @param imageBlendData1 成员变量都要有正确值
     * @param imageBlendData  zoom要有正确值,在imageBlendData0和imageBlendData1的zoom之间
     */
    private static void getImageBlendData(@NonNull ImageBlendData imageBlendData0, @NonNull ImageBlendData imageBlendData1,
                                          @NonNull ImageBlendData imageBlendData) {

        //    z: zoom    s: scale
        //
        //              1s             0s
        //             /|              |\
        //          s/  |              |  \s
        //         /|   |              |   |\
        //     0s/  |   |              |   |  \1s
        //    ___|__|___|___        ___|___|__|___
        //      0z  z   1z             0z  z  1z
        //
        //    int zoomDifference = 1z - 0z;
        //    int scaleDifference = 1s - 0s;
        //    int xDifference = z - 0z;
        //    ...

        float zoomDifference = imageBlendData1.zoom - imageBlendData0.zoom;
        float scaleDifference = imageBlendData1.previewView1SizeScale - imageBlendData0.previewView1SizeScale;
        float xDifference = imageBlendData.zoom - imageBlendData0.zoom;
        float yDifference = scaleDifference * xDifference / zoomDifference;

        imageBlendData.previewView1SizeScale = imageBlendData0.previewView1SizeScale + yDifference;
    }
}
