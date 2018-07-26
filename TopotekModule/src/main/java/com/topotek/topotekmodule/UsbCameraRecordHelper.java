package com.topotek.topotekmodule;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.os.Handler;
import android.os.Looper;

import com.topotek.module.android.debug.Debugger;
import com.topotek.module.android.thread.HandlerThreadUtils;

import java.io.IOException;
import java.nio.ByteBuffer;

public class UsbCameraRecordHelper {

    private static MediaCodec videoEncoder;
    private static MediaMuxer mediaMuxer;
    private static int videoTrackIndex;
    private static Handler recordHandler;

    public static void initRecord(String videoFilePath, int width, int height) {

        try {
            videoEncoder = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_VIDEO_AVC);
        } catch (IOException e) {
            e.printStackTrace();
        }

        MediaFormat videoFormat = MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_AVC, width, height);//MIMETYPE_VIDEO_MPEG4
        videoFormat.setInteger(MediaFormat.KEY_BIT_RATE, 2500000);
        videoFormat.setInteger(MediaFormat.KEY_FRAME_RATE, 10);
        videoFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_Format32bitARGB8888);
        videoFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 1);

        videoEncoder.configure(videoFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
        videoEncoder.start();

        frameDataSign = 0;
        ++frameDataSign;//===========================-------------------------------------============

        try {
            mediaMuxer = new MediaMuxer(videoFilePath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(recordHandler == null){
            Looper looper = HandlerThreadUtils.startNewHandlerThread("usbCameraRecord");
            recordHandler = new Handler(looper);
        }

        isStopRecord = false;
        isStopOk = false;
    }

    public static void putFrameData(final byte[] frameData) {
        recordHandler.post(new Runnable() {
            @Override
            public void run() {
                putFrameData_private(frameData);
            }
        });
    }

    private static RecordStopListener mRecordStopListener;

    public static void stopRecord(RecordStopListener recordStopListener) {
        mRecordStopListener = recordStopListener;
        recordHandler.post(new Runnable() {
            @Override
            public void run() {
                isStopRecord = true;
                getData();
            }
        });
    }

    private static void putFrameData_private(byte[] frameData) {
        int inputBufferIndex = videoEncoder.dequeueInputBuffer(-1);
        if (inputBufferIndex >= 0) {
            ByteBuffer inputBuffer = videoEncoder.getInputBuffer(inputBufferIndex);
            inputBuffer.clear();
            inputBuffer.put(frameData);
            ++frameDataSign;
            videoEncoder.queueInputBuffer(inputBufferIndex, 0, frameData.length, System.nanoTime() / 1000, 0);

            getData();
        }
    }

    private static int frameDataSign = 0;
    private static boolean isStopRecord = false;
    private static boolean isStopOk = false;

    private static void getData() {
        MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
        int outputBufferIndex = videoEncoder.dequeueOutputBuffer(bufferInfo, 0);
        if (outputBufferIndex >= 0) {
            --frameDataSign;
            ByteBuffer outputBuffer = videoEncoder.getOutputBuffer(outputBufferIndex);
            mediaMuxer.writeSampleData(videoTrackIndex, outputBuffer, bufferInfo);
            videoEncoder.releaseOutputBuffer(outputBufferIndex, false);

//            Debugger.show(outputBufferIndex + "    frameDataSign:" + frameDataSign);//---------=====--------

            if (isStopRecord && frameDataSign < 1 && !isStopOk) {
                Debugger.addShow("  @stop");
                isStopOk = true;
                videoEncoder.stop();
                videoEncoder.release();
                mediaMuxer.stop();
                mediaMuxer.release();
                mRecordStopListener.onRecordStop();
            }
            return;
        } else if (outputBufferIndex == MediaCodec.INFO_TRY_AGAIN_LATER) {

        } else if (outputBufferIndex == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
            videoTrackIndex = mediaMuxer.addTrack(videoEncoder.getOutputFormat());
            mediaMuxer.start();
        } else if (outputBufferIndex == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {

        }

        if (isStopRecord && frameDataSign < 1 && !isStopOk) {
            Debugger.addShow("  #stop");
            isStopOk = true;
            videoEncoder.stop();
            videoEncoder.release();
            mediaMuxer.stop();
            mediaMuxer.release();
            mRecordStopListener.onRecordStop();
            return;
        }

        recordHandler.post(new Runnable() {
            @Override
            public void run() {
                getData();
            }
        });
    }

    public interface RecordStopListener {
        void onRecordStop();
    }
}
