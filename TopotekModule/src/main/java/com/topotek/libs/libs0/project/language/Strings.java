package com.topotek.libs.libs0.project.language;

public class Strings {

    public static final int Language_Chinses = 0;
    public static final int Language_English = 1;

    public static int language = Language_Chinses;

    public static String 语言() {
        return new String[]{"语言", "Language"}[language];
    }

    public static String 图像设置() {
        return new String[]{"图像设置", "Image Setting"}[language];
    }

    public static String 拍照录像() {
        return new String[]{"拍照录像", "Photo&Video"}[language];
    }

    public static String 照片分辨率() {

        return new String[]{"照片分辨率", "Photo Resolution"}[language];
    }

    public static String 录像分辨率() {
        return new String[]{"录像分辨率", "Video Resolution"}[language];
    }

    public static String 拍照设备() {
        return new String[]{"拍照设备", "Photo Device"}[language];
    }

    public static String 主摄像头() {
        return new String[]{"主摄像头", "Main Camera"}[language];
    }

    public static String 副摄像头() {
        return new String[]{"副摄像头", "Secondary Camera"}[language];
    }

    public static String 主加副() {
        return new String[]{"主 + 副", "Main + Secondary"}[language];
    }

    public static String 录像设备() {
        return new String[]{"录像设备", "Video Device"}[language];
    }

    public static String 系统设置() {
        return new String[]{"系统设置", "System Setting"}[language];
    }

    public static String 时间设置() {
        return new String[]{"时间设置", "Time Setting"}[language];
    }

    public static String 年() {
        return new String[]{"年", "-"}[language];
    }

    public static String 月() {
        return new String[]{"月", "-"}[language];
    }

    public static String 日() {
        return new String[]{"日", " "}[language];
    }

    public static String 时() {
        return new String[]{"时", ":"}[language];
    }

    public static String 分() {
        return new String[]{"分", ":"}[language];
    }

    public static String 秒() {
        return new String[]{"秒", " "}[language];
    }

    public static String 版本信息() {
        return new String[]{"版本信息", "Version"}[language];
    }

    public static String 未检测到TF卡() {
        return new String[]{"未检测到TF卡", "NO TF Card"}[language];
    }

    public static String 未检测到() {
        return new String[]{"未检测到", "Undetected"}[language];
    }

    public static String 正在检测() {
        return new String[]{"正在检测", "Detecting"}[language];
    }

    public static String 未挂载() {
        return new String[]{"未挂载", "Unmounted"}[language];
    }

    public static String 无法挂载() {
        return new String[]{"无法挂载", "Unable to Mount"}[language];
    }

    public static String 没有文件系统或不支持() {
        return new String[]{"没有文件系统或不支持", "No File System or Unsupport"}[language];
    }

    public static String 未知状态() {
        return new String[]{"未知状态", "Unknown Status"}[language];
    }

    public static String 异常() {
        return new String[]{"异常", "Abnormal"}[language];
    }

    public static String 打开相机出错() {
        return new String[]{"打开相机出错", "Open Camera Error"}[language];
    }

    public static String 打开相机失败() {
        return new String[]{"打开相机失败", "Open Camera Failed"}[language];
    }

    public static String 相机异常() {
        return new String[]{"相机异常", "Camera Abnormal"}[language];
    }

    public static String 设置预览出错() {
        return new String[]{"设置预览出错", "Preview Setting Error"}[language];
    }

    public static String 预览相机出错() {
        return new String[]{"预览相机出错", "Camera Preview Error"}[language];
    }

    public static String 未检测到主摄像头() {
        return new String[]{"未检测到主摄像头", "Main Camera Undetected"}[language];
    }
}
