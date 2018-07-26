package com.topotek.module.project.storageDevice;

import java.io.File;

//ok
public class TfCardHelper {

    private static File tfCardDirectory;

    /**
     * 该方法平台相关
     * <p>
     * TF卡不可热插拔
     *
     * @return null: 未检测到TF卡  else: 返回的文件对象一定存在，且是一个目录文件
     */
    public static File getTfCardDirectory() {

        if (tfCardDirectory == null)
            tfCardDirectory = TfCardUtils.getTfCardDirectory();

        return tfCardDirectory;
    }
}
