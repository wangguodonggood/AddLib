package com.topotek.module.java.file;

import java.io.File;

//ok
public class SpecialReadFileUtils {

    /**
     * 不检查参数
     *
     * @return -1: 创建流或读失败  -2: 读到空字符串  -3: 读到的不是整数  -4: 读到的是负数  0: 读到的是0  else: 读到的是正整数
     */
    public static int readPositiveInteger(File file, int cbufLen) {

        String s = SimpleReadFileUtils.readFile(file, cbufLen);
        if (s == null)
            return -1;
        if ("".equals(s))
            return -2;

        int i;
        try {
            i = Integer.parseInt(s);
        } catch (Exception e) {
            e.printStackTrace();
            return -3;
        }

        if (i < 0)
            return -4;

        return i;
    }
}
