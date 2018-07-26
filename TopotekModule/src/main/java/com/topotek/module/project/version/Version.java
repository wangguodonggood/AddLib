package com.topotek.module.project.version;

public class Version {

    public static String versionFileHead = "setup";

    public static String modelCode = "SMT";

    public static int versionCode = 100;

    public static String versionFileTail = ".topotek";

    public static String[] description = {""};

    public static String versionToString() {
        return modelCode + versionCode + description[0];
    }
}