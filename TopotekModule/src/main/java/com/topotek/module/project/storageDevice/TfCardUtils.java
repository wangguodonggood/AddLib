package com.topotek.module.project.storageDevice;

import android.os.Build;

import java.io.File;

//ok
class TfCardUtils {

    private static boolean isMyAndroid6() {
        return Build.VERSION.SDK_INT == Build.VERSION_CODES.M;
    }

    private static boolean isMyAndroid5_1() {
        return Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP_MR1;
    }

    /**
     * 该方法平台相关
     * <p>
     * TF卡不可热插拔
     *
     * @return null: 未检测到TF卡  else: 返回的文件对象一定存在，且是一个目录文件
     */
    private static File getTfCardDirectory_MyAndroid6() {

        File file = new File("/storage");
        if (file.exists()) {

            File[] files = file.listFiles();
            if (files != null && files.length >= 3) {

                for (File directory : files) {

                    switch (directory.getAbsolutePath()) {
                        case "/storage/emulated":
                        case "/storage/self":
                        case "/storage/sdcard0":
                        case "/storage/sdcard1":
                            break;
                        default:
                            if (directory.exists() && directory.isDirectory())
                                return directory;
                            break;
                    }
                }
            }
        }

        return null;
    }

    /**
     * 该方法平台相关
     * <p>
     * TF卡不可热插拔
     *
     * @return null: 未检测到TF卡  else: 返回的文件对象一定存在，且是一个目录文件
     */
    private static File getTfCardDirectory_MyAndroid5_1() {

        File file = new File("/storage/sdcard1");

        if (file.exists() && file.listFiles() != null)
            return file;
        else
            return null;
    }

    /**
     * 该方法平台相关
     * <p>
     * TF卡不可热插拔
     * <p>
     * 自动判断聪目安卓系统版本
     *
     * @return null: 未检测到TF卡  else: 返回的文件对象一定存在，且是一个目录文件
     */
    static File getTfCardDirectory() {

        if (isMyAndroid6())
            return getTfCardDirectory_MyAndroid6();
        else if (isMyAndroid5_1())
            return getTfCardDirectory_MyAndroid5_1();
        else
            return null;
    }
}
