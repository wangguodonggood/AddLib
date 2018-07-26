#ifndef NEWCAMERA_V4L2UTILS_H
#define NEWCAMERA_V4L2UTILS_H

#include <fcntl.h>
#include <linux/videodev2.h>
#include <sys/mman.h>

static int width;
static int height;
int initVideo(int fd){

    width = 640;
    height = 480;

    struct v4l2_capability capability;
    //查询capability
    if(ioctl(fd, VIDIOC_QUERYCAP, &capability) < 0)
        return -1;
    if(!(capability.capabilities & V4L2_CAP_VIDEO_CAPTURE))//看是否支持V4L2_CAP_VIDEO_CAPTURE
        return -2;//不支持V4L2_CAP_VIDEO_CAPTURE
    //====================================================================================================
    int isFrameSizeSupport = 0;
    int isTopotek1032 = 0;
    int isUsbCameraS = 0;
    struct v4l2_fmtdesc fmtdesc;
    memset(&fmtdesc, 0, sizeof(fmtdesc));
    fmtdesc.type = V4L2_BUF_TYPE_VIDEO_CAPTURE;
    struct v4l2_frmsizeenum frmsizeenum;
    memset(&frmsizeenum, 0, sizeof(frmsizeenum));
    for(fmtdesc.index = 0 ;ioctl(fd, VIDIOC_ENUM_FMT, &fmtdesc) >= 0; ++fmtdesc.index){
        frmsizeenum.pixel_format = fmtdesc.pixelformat;
        if(fmtdesc.pixelformat == V4L2_PIX_FMT_YUYV){
            debug_addShow(getJniEnv(), 20, "\nfmt: YUYV ")
        } else if(fmtdesc.pixelformat == V4L2_PIX_FMT_MJPEG){
            debug_addShow(getJniEnv(), 20, "\nfmt: MJPEG ")
        } else{
            debug_addShow(getJniEnv(), 20, "\nfmt:%d ", fmtdesc.pixelformat)
        }
        for(frmsizeenum.index = 0 ;ioctl(fd, VIDIOC_ENUM_FRAMESIZES, &frmsizeenum) >= 0; ++frmsizeenum.index){
            if(frmsizeenum.type == V4L2_FRMSIZE_TYPE_DISCRETE){
                debug_addShow(getJniEnv(), 20, "  %d*%d", frmsizeenum.discrete.width, frmsizeenum.discrete.height)
                if(width == frmsizeenum.discrete.width && height == frmsizeenum.discrete.height)
                    isFrameSizeSupport = 1;
                if(frmsizeenum.discrete.width == 384 && frmsizeenum.discrete.height == 288)
                    isUsbCameraS = 1;
            } else{
                debug_addShow(getJniEnv(), 5, "  *")
            }
        }
    }
    if(fmtdesc.index != 1 && frmsizeenum.index != 5 && frmsizeenum.discrete.width == 800 && frmsizeenum.discrete.height == 600)
        isTopotek1032 = 1;
    debug_addShow(getJniEnv(), 5, "\n");

    if(isTopotek1032){
        if (!isFrameSizeSupport){
            width = 640;
            height = 480;
        }
    } else{
        if(isUsbCameraS){
            width = 720;
            height = 576;
        } else{
            if (!isFrameSizeSupport){
                width = 160;
                height = 120;
            }
        }
    }

    fmtdesc.pixelformat = V4L2_PIX_FMT_MJPEG;//----------------------------------------------------------------

    if(fmtdesc.pixelformat == V4L2_PIX_FMT_YUYV){
        debug_addShow(getJniEnv(), 20, "YUYV %d*%d", width, height)
    } else if(fmtdesc.pixelformat == V4L2_PIX_FMT_MJPEG){
        debug_addShow(getJniEnv(), 20, "MJPEG %d*%d", width, height)
    }
    //====================================================================================================



    struct v4l2_format format;
    memset(&format, 0, sizeof(format));//清空脏内存
    format.type = V4L2_BUF_TYPE_VIDEO_CAPTURE;
    format.fmt.pix.pixelformat = fmtdesc.pixelformat;
    format.fmt.pix.width = (__u32) width;
    format.fmt.pix.height = (__u32) height;
    //尝试设置该格式
    if(ioctl(fd, VIDIOC_TRY_FMT, &format) < 0)
        return -3;
    //设置该格式
    if(ioctl(fd, VIDIOC_S_FMT, &format) < 0)
        return -4;

    return 1;
}

int v4l2Utils_VIDIOC_REQBUFS(int fd, __u32 buffersCount){
    
    struct v4l2_requestbuffers requestBuffers;
    requestBuffers.type = V4L2_BUF_TYPE_VIDEO_CAPTURE;
    requestBuffers.count = buffersCount;
    requestBuffers.memory = V4L2_MEMORY_MMAP;
    //请求缓冲区
    return ioctl(fd, VIDIOC_REQBUFS, &requestBuffers);
}

int v4l2Utils_VIDIOC_QUERYBUF(int fd, struct v4l2_buffer *buffer, __u32 index){
    
    memset(buffer, 0, sizeof(*buffer));

    (*buffer).type = V4L2_BUF_TYPE_VIDEO_CAPTURE;
    (*buffer).memory = V4L2_MEMORY_MMAP;
    (*buffer).index = index;
    //查询缓冲区
    return ioctl(fd, VIDIOC_QUERYBUF, buffer);
}

int v4l2Utils_setBufs(int fd, void *void0[], struct v4l2_buffer *buffer, __u32 buffersCount){
    __u32 index;
    for(index = 0; index < buffersCount; ++index){
        
        //查询缓冲区
        if(v4l2Utils_VIDIOC_QUERYBUF(fd, buffer, index) < 0)
            return -6;

        void0[(*buffer).index] = mmap(NULL, (*buffer).length, PROT_READ | PROT_WRITE, MAP_SHARED, fd, (off_t) (*buffer).m.offset);
        if(void0[(*buffer).index] == MAP_FAILED)
            return -7;

        //缓冲区入队
        if(ioctl(fd, VIDIOC_QBUF, buffer) < 0)
            return -8;
    }

    return 1;
}

int v4l2Utils_initBufs(int fd, void *void0[], struct v4l2_buffer *buffer, __u32 buffersCount){

    if(v4l2Utils_VIDIOC_REQBUFS(fd, buffersCount) < 0)
        return -5;

    return v4l2Utils_setBufs(fd, void0, buffer, buffersCount);
}

#endif //NEWCAMERA_V4L2UTILS_H
