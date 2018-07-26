package com.topotek.module.java.file;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

//ok
public class SimpleWriteFileUtils {

    /**
     * 不检查参数
     *
     * @return 0: 文件已存在,不需要创建    -1: 创建出错    -2: 创建失败    1: 创建成功
     */
    public static int createFile(File file) {

        if (file.exists())
            return 0;
        else {

            boolean isCreateNewFileOk;
            try {
                isCreateNewFileOk = file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return -1;
            }

            if (!isCreateNewFileOk)
                return -2;
        }

        return 1;
    }

    /**
     * 不检查参数
     * <p>
     * 覆盖写文件
     * <p>
     * 文件不存在则自动创建
     *
     * @return -1: 创建文件出错  -2: 创建文件失败  -3: 创建流失败  -4: 写失败  0: 关流失败  1: 成功
     */
    public static int writeFile(File file, char[] cbuf, int off, int len) {

        int i = createFile(file);
        if (i < 0)
            return i;

        Writer writer;
        try {
            writer = new FileWriter(file);
        } catch (IOException e) {
            e.printStackTrace();
            return -3;
        }

        try {
            writer.write(cbuf, off, len);
        } catch (IOException e) {
            e.printStackTrace();
            try {
                writer.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return -4;
        }

        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }

        return 1;
    }

    /**
     * 不检查参数
     * <p>
     * 覆盖写文件
     * <p>
     * 文件不存在则自动创建
     *
     * @return -1: 创建文件出错  -2: 创建文件失败  -3: 创建流失败  -4: 写失败  0: 关流失败  1: 成功
     */
    public static int writeFile(File file, String str, int off, int len) {

        int i = createFile(file);
        if (i < 0)
            return i;

        Writer writer;
        try {
            writer = new FileWriter(file);
        } catch (IOException e) {
            e.printStackTrace();
            return -3;
        }

        try {
            writer.write(str, off, len);
        } catch (IOException e) {
            e.printStackTrace();
            try {
                writer.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return -4;
        }

        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }

        return 1;
    }

    /**
     * 不检查参数
     * <p>
     * 覆盖写文件
     * <p>
     * 文件不存在则自动创建
     *
     * @return -1: 创建文件出错  -2: 创建文件失败  -3: 创建流失败  -4: 写失败  0: 关流失败  1: 成功
     */
    public static int writeFile(File file, char[] cbuf) {
        return writeFile(file, cbuf, 0, cbuf.length);
    }

    /**
     * 不检查参数
     * <p>
     * 覆盖写文件
     * <p>
     * 文件不存在则自动创建
     *
     * @return -1: 创建文件出错  -2: 创建文件失败  -3: 创建流失败  -4: 写失败  0: 关流失败  1: 成功
     */
    public static int writeFile(File file, String str) {
        return writeFile(file, str, 0, str.length());
    }

    /**
     * 不检查参数
     * <p>
     * 覆盖写文件
     * <p>
     * 文件不存在则自动创建
     *
     * @return -1: 创建文件出错  -2: 创建文件失败  -3: 创建流失败  -4: 写失败  0: 关流失败  1: 成功
     */
    public static int writeFile(File file, byte[] bytes, int off, int len) {

        int i = createFile(file);
        if (i < 0)
            return i;

        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return -3;
        }
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);

        try {
            bufferedOutputStream.write(bytes, off, len);
        } catch (IOException e) {
            e.printStackTrace();
            try {
                bufferedOutputStream.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return -4;
        }

        try {
            bufferedOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }

        return 1;
    }

    /**
     * 不检查参数
     * <p>
     * 覆盖写文件
     * <p>
     * 文件不存在则自动创建
     *
     * @return -1: 创建文件出错  -2: 创建文件失败  -3: 创建流失败  -4: 写失败  0: 关流失败  1: 成功
     */
    public static int writeFile(File file, byte[] bytes) {
        return writeFile(file, bytes, 0, bytes.length);
    }
}