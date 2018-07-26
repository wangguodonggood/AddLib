package com.topotek.topotekmodule.module.media.mediaCodec;

import android.media.MediaCodecInfo;

import com.topotek.module.android.debug.Debugger;
import com.topotek.module.android.media.mediaCodec.MediaCodecInfoTool;

public class MediaCodecInfoUtils {

    public static void traversal() {

        MediaCodecInfoTool.traversalMediaCodecList(
                new MediaCodecInfoTool.TraversalCallback1() {
            @Override
            public void onMediaCodecInfo(MediaCodecInfo mediaCodecInfo, final String name, boolean isEncoder) {
                if (isEncoder)
                    MediaCodecInfoTool.traversalMediaCodecInfo(mediaCodecInfo, new MediaCodecInfoTool.TraversalCallback2() {
                        @Override
                        public boolean onVideoCapabilities(MediaCodecInfo.CodecCapabilities codecCapabilities, MediaCodecInfo.VideoCapabilities videoCapabilities) {
                            Debugger.addShow("\n" + name + "    " + codecCapabilities.getMimeType());
                            if (videoCapabilities != null) {
                                boolean sizeSupported = videoCapabilities.isSizeSupported(640, 480);
                                Debugger.addShow("    640sizeSupported:" + sizeSupported + "\n");
                            }
                            return true;
                        }
                        @Override
                        public boolean onColorFormats(MediaCodecInfo.CodecCapabilities codecCapabilities, int[] colorFormats) {
                            for (int colorFormat : colorFormats) {
                                if (colorFormat >= 0x7F000000)
                                    Debugger.addShow("  @" + Integer.toHexString(colorFormat));
                                else
                                    Debugger.addShow("  @" + colorFormat);
                            }
                            return true;
                        }
                    });
            }
        });
    }
}
