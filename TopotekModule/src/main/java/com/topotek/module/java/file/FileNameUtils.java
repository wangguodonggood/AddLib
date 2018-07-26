package com.topotek.module.java.file;

import java.io.File;

//ok
public class FileNameUtils {

    /**
     * 文件防重名
     * <p>
     * 不检查参数
     * <p>
     * file.txt  if exists→  file_2.txt  if exists→  file_3.txt  if exists→  ...  file_max.txt
     *
     * @param fileAbsolutePathNameHead 文件全路径名去掉后缀和点
     * @param fileNameTail             点+文件后缀
     * @param max                      最小传2
     * @return 返回没有重名的文件，无法不重名则返回null
     */
    public static File noTautonomy(String fileAbsolutePathNameHead, String fileNameTail, int max) {

        File file = new File(fileAbsolutePathNameHead + fileNameTail);
        if (file.exists()) {

            fileAbsolutePathNameHead = fileAbsolutePathNameHead + "_";
            int i = 2;
            file = new File(fileAbsolutePathNameHead + i + fileNameTail);
            while (file.exists()) {

                if (i >= max)
                    return null;

                file = new File(fileAbsolutePathNameHead + ++i + fileNameTail);
            }
        }

        return file;
    }
}
