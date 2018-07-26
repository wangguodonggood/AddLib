#ifndef TOPOTEKCAMERA_AVIUTILS_H
#define TOPOTEKCAMERA_AVIUTILS_H

#include "avilib.h"

avi_t *out_fp;

static int isVideoInit = 0;

int avilibUtils_isVideoInit(){
    return isVideoInit;
}

int video_writer_init(char *file_name, int width, int height, char *out_fmt){//out_fmt  "YUYV" / "MJPG" / "h264"

    out_fp = AVI_open_output_file(file_name);
    if(out_fp == 0)
        return -1;

    AVI_set_video(out_fp, width, height, 10, out_fmt);

    isVideoInit = 1;

    return 1;
}

int video_writer_uninit(){

    isVideoInit = 0;

    if(AVI_close(out_fp) < 0) {
        return -1;
    }

    return 1;
}

int video_write(char *frame, int length){

    if(AVI_write_frame(out_fp, frame, length) < 0)
        return -1;

    return 1;
}

#endif //TOPOTEKCAMERA_AVIUTILS_H
