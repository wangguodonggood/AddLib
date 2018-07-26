package com.topotek.module.android.bitMap;

import android.graphics.Bitmap;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

//ok
public class BitMapUtils {

    /**
     * 不检查参数
     *
     * @return -1: 创建流失败    0: bitmap.compress = false    1: bitmap.compress = true
     */
    public static int compressToFile(Bitmap bitmap, File file, Bitmap.CompressFormat compressFormat, int quality) {

        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);

        boolean compress = bitmap.compress(compressFormat, quality, bufferedOutputStream);

        try {
            bufferedOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return compress ? 1 : 0;
    }
}
