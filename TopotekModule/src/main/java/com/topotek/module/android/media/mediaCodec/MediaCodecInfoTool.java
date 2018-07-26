package com.topotek.module.android.media.mediaCodec;

import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.media.MediaFormat;

public class MediaCodecInfoTool {

    /**
     * 不检查参数
     */
    public static void traversalMediaCodecList(TraversalCallback1 traversalCallback1) {

        for (int i = 0; i < MediaCodecList.getCodecCount(); ++i) {

            MediaCodecInfo mediaCodecInfo = MediaCodecList.getCodecInfoAt(i);

            String name = mediaCodecInfo.getName();
            boolean isEncoder = mediaCodecInfo.isEncoder();

            traversalCallback1.onMediaCodecInfo(mediaCodecInfo, name, isEncoder);
        }
    }

    /**
     * 不检查参数
     */
    public static void traversalMediaCodecInfo(MediaCodecInfo mediaCodecInfo, TraversalCallback2 traversalCallback2) {

        for (String supportedType : mediaCodecInfo.getSupportedTypes()) {

            MediaCodecInfo.CodecCapabilities codecCapabilities = mediaCodecInfo.getCapabilitiesForType(supportedType);

            String mimeType = codecCapabilities.getMimeType();
            MediaFormat defaultFormat = codecCapabilities.getDefaultFormat();

            traversalCallback2.onCodecCapabilities(supportedType, codecCapabilities, defaultFormat);

            if (mediaCodecInfo.isEncoder()) {
                MediaCodecInfo.EncoderCapabilities encoderCapabilities = codecCapabilities.getEncoderCapabilities();
                boolean b = traversalCallback2.onEncoderCapabilities(codecCapabilities, encoderCapabilities);
                if (!b)
                    continue;
            }

            if (supportedType.startsWith("video") || mimeType.startsWith("video")) {
                MediaCodecInfo.VideoCapabilities videoCapabilities = codecCapabilities.getVideoCapabilities();
                boolean b = traversalCallback2.onVideoCapabilities(codecCapabilities, videoCapabilities);
                if (!b)
                    continue;
            }

            if (supportedType.startsWith("audio") || mimeType.startsWith("audio")) {
                MediaCodecInfo.AudioCapabilities audioCapabilities = codecCapabilities.getAudioCapabilities();
                boolean b = traversalCallback2.onAudioCapabilities(codecCapabilities, audioCapabilities);
                if (!b)
                    continue;
            }

            int[] colorFormats = codecCapabilities.colorFormats;
            boolean b = traversalCallback2.onColorFormats(codecCapabilities, colorFormats);
            if (!b)
                continue;
        }
    }

    public interface TraversalCallback1 {

        void onMediaCodecInfo(MediaCodecInfo mediaCodecInfo, String name, boolean isEncoder);
    }

    public static abstract class TraversalCallback2 {

        public boolean onCodecCapabilities(String supportedType, MediaCodecInfo.CodecCapabilities codecCapabilities, MediaFormat defaultFormat) {
            return true;
        }

        public boolean onEncoderCapabilities(MediaCodecInfo.CodecCapabilities codecCapabilities, MediaCodecInfo.EncoderCapabilities encoderCapabilities) {
            return true;
        }

        public boolean onVideoCapabilities(MediaCodecInfo.CodecCapabilities codecCapabilities, MediaCodecInfo.VideoCapabilities videoCapabilities) {
            return true;
        }

        public boolean onAudioCapabilities(MediaCodecInfo.CodecCapabilities codecCapabilities, MediaCodecInfo.AudioCapabilities audioCapabilities) {
            return true;
        }

        public boolean onColorFormats(MediaCodecInfo.CodecCapabilities codecCapabilities, int[] colorFormats) {
            return true;
        }
    }
}
