package com.topotek.topotekmodule.cameraManage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.SystemClock;
import android.util.Size;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.topotek.module.android.debug.Debugger;

public class CameraCaptureUtils {

    public static boolean isInverseOrientation = false;//------------------------------------------------

    static void savePicture(final byte[] data, File outputFile) {

        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream(outputFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        if(isInverseOrientation){

            long a = SystemClock.elapsedRealtime();

            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);

            long b = SystemClock.elapsedRealtime();

            Matrix matrix = new Matrix();
//            matrix.setRotate(180);
            matrix.setScale(-1F, -1F);
            Bitmap bitmap1 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
//            Bitmap bitmap1 = Bitmap.createScaledBitmap(bitmap, -bitmap.getWidth(), -bitmap.getHeight(), true);

            long c = SystemClock.elapsedRealtime();

            bitmap1.compress(Bitmap.CompressFormat.JPEG, 90, bufferedOutputStream);

            long d = SystemClock.elapsedRealtime();
            Debugger.addShow((b-a) +"    "+ (c-b) +"    "+ (d-c) + "\n");

            bitmap.recycle();
            bitmap1.recycle();
            try {
                bufferedOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else {

            try {
            fileOutputStream.write(data);
            fileOutputStream.close();
            } catch (IOException e) {
            e.printStackTrace();
            }
        }
    }

    static void setMediaRecorder(MediaRecorder mediaRecorder, Size size, File outputFile) {

        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
//        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);//------------------------------------------------------------h
//        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);//----------------------------------------------------h

        CamcorderProfile mCamcorderProfile = CamcorderProfile.get(Camera.CameraInfo.CAMERA_FACING_BACK, CamcorderProfile.QUALITY_HIGH);
        mCamcorderProfile.videoFrameWidth = size.getWidth();
        mCamcorderProfile.videoFrameHeight = size.getHeight();
        mediaRecorder.setProfile(mCamcorderProfile);

//        mediaRecorder.setVideoSize(size.getWidth(), size.getHeight());//------------------------------------------------------------h
//        mediaRecorder.setVideoSize(640, 480);//----------------------------------------------------h

        mediaRecorder.setOutputFile(outputFile.getAbsolutePath());

        if(isInverseOrientation)
            mediaRecorder.setOrientationHint(180);
    }
}
