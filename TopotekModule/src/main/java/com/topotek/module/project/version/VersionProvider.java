package com.topotek.module.project.version;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.topotek.module.project.version.Version;

public class VersionProvider extends ContentProvider {

    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        if(Version.modelCode == null || Version.modelCode.length() < 1)
            return null;
        if(Version.versionCode < 1)
            return null;
        if(Version.versionFileTail == null || Version.versionFileTail.length() < 1)
            return null;

        /*setupSMT@100@.topotek */
        return Version.versionFileHead + Version.modelCode + '@'
                + Version.versionCode + '@' + Version.versionFileTail;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
