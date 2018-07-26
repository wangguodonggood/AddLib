package com.topotek.module.java.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

//ok
public class SimpleReadFileUtils {

    /**
     * 不检查参数
     * <p>
     * 读一次文件
     * <p>
     * 关流失败未处理
     *
     * @return -1: 创建流失败  -2: 读失败  else: 读到的字符个数
     */
    public static int readFile(File file, char[] cbuf, int off, int len) {

        Reader reader;
        try {
            reader = new FileReader(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return -1;
        }

        int read;
        try {
            read = reader.read(cbuf, off, len);
        } catch (IOException e) {
            e.printStackTrace();
            return -2;
        }finally {

            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return read;
    }

    /**
     * 不检查参数
     * <p>
     * 读一次文件
     * <p>
     * 关流失败未处理
     *
     * @return -1: 创建流失败  -2: 读失败  else: 读到的字符个数
     */
    public static int readFile(File file, char[] cbuf) {
        return readFile(file, cbuf, 0, cbuf.length);
    }

    /**
     * 不检查参数
     * <p>
     * 读一次文件
     * <p>
     * 关流失败未处理
     *
     * @return null: 创建流或读失败  else: 读到的字符串
     */
    public static String readFile(File file, int cbufLen) {

        char[] cbuf = new char[cbufLen];

        int i = readFile(file, cbuf);
        if (i < 0)
            return null;
        else if (i == 0)
            return "";
        else
            return String.valueOf(cbuf, 0, i);
    }
}