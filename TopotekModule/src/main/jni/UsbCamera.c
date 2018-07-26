#include <unistd.h>
#include "jni_UsbCamera.h"

#include "module/c/colorUtils/colorUtils.h"
#include "module/project/avilib/aviutils.h"

#include "debug/debugTools.h"

#include "v4l2Utils.h"

//int setUsbCamera(int fd, int buffersCount, void *void0[], struct v4l2_buffer *buffer){
//    if(format.fmt.pix.bytesperline < width * 2){//--------------------------------------------------------------
//        format.fmt.pix.bytesperline = (__u32) (width * 2);
//    }
//    if(format.fmt.pix.sizeimage < width * 2 * height){
//        format.fmt.pix.sizeimage = (__u32) (width * 2 * height);
//    }//----------------------------------------------------------------------------------------------------------
//}

//int init(JNIEnv *jniEnv0, jobject jobject0, int fd, jint buffersCount, jbyteArray jbyteArray0){
//    jmethodID jmethodID2 = (*jniEnv0)->GetMethodID(jniEnv0, jclass0, "getOutputFileName", "()Ljava/lang/String;");
//        if(isRecord){
//            if(avilibUtils_isVideoInit()){
//                if(jboolean1 == JNI_TRUE){
//                    if(video_write((char *) rgbFrameData, buffer.length) < 0)
//                    debug_addShow(jniEnv0, 40, "    usbCamera_Video_write error")
//                }
//            } else{
//                jstring jstring1 = (*jniEnv0)->CallObjectMethod(jniEnv0, jobject0, jmethodID2);
//                const char *fileName = (*jniEnv0)->GetStringUTFChars(jniEnv0, jstring1, 0);
//                int i01 = video_writer_init((char *) fileName, width, height, "MJPG");
//                (*jniEnv0)->ReleaseStringUTFChars(jniEnv0, jstring1, fileName);
//                debug_addShow(jniEnv0, 40, "    usbCameraRecordInit %d", i01)
//            }
//        } else{
//            if(avilibUtils_isVideoInit()){
//                int i02 = video_writer_uninit();
//                debug_addShow(jniEnv0, 40, "    usbCameraRecordUninit %d", i02)
//            }
//        }
//}

static int fd;

jint Java_com_topotek_topotekmodule_cameraModule_CameraModule_jniVideoWriterInit(JNIEnv *jniEnv0, jclass jclass0, jstring videoFilePath){

    const char *string = (*jniEnv0)->GetStringUTFChars(jniEnv0, videoFilePath, 0);
    int i = video_writer_init((char *) string, width, height, "MJPG");
    (*jniEnv0)->ReleaseStringUTFChars(jniEnv0, videoFilePath, string);
    debug_addShow(jniEnv0, 40, "    usbCameraRecordInit %d", i)
    return i;

}

jint Java_com_topotek_topotekmodule_cameraModule_CameraModule_jniVideoWriterUninit(JNIEnv *jniEnv0, jclass jclass0){
    int i = video_writer_uninit();
    debug_addShow(jniEnv0, 40, "    usbCameraRecordUninit %d", i)
    return i;
}

jint Java_com_topotek_topotekmodule_cameraModule_CameraModule_jniVideoWriter(JNIEnv *jniEnv0, jclass jclass0, jbyteArray frameData, jint length){

    jbyte *data = (*jniEnv0)->GetByteArrayElements(jniEnv0, frameData, 0);
    int i = video_write((char *) data, length);
    (*jniEnv0)->ReleaseByteArrayElements(jniEnv0, frameData, data, 0);
    if(i < 0)
    debug_addShow(jniEnv0, 40, "    usbCamera_Video_write error%d", i);

    return i;
}

jint Java_com_topotek_topotekmodule_cameraModule_CameraModule_jniInitUsbCamera
        (JNIEnv *jniEnv0, jclass jclass0, jstring usbCameraDeviceFilePath){//----------------------------======---------=====

    const char *string = (*jniEnv0)->GetStringUTFChars(jniEnv0, usbCameraDeviceFilePath, 0);
    fd = open(string, O_RDWR);
    (*jniEnv0)->ReleaseStringUTFChars(jniEnv0, usbCameraDeviceFilePath, string);

    debug_addShow(jniEnv0, 20, "    fd = %d", fd);

    if(fd < 0)
        return -128;
    int i = initVideo(fd);
    if(i < 0)
        close(fd);
    return i;
}

jint Java_com_topotek_topotekmodule_cameraModule_CameraModule_jniUsbCameraStreamOn
        (JNIEnv *jniEnv0, jclass jclass0, jint buffersCount, jbyteArray jbyteArray0){

    void *void0[buffersCount];
    struct v4l2_buffer buffer;

    //初始缓冲区
    int i1 = v4l2Utils_initBufs(fd, void0, &buffer, (__u32) buffersCount);
    if(i1 < 0)
        return i1;

    struct v4l2_streamparm streamparm;
    memset(&streamparm, 0, sizeof(streamparm));
    streamparm.type = V4L2_BUF_TYPE_VIDEO_CAPTURE;
    if(ioctl(fd, VIDIOC_G_PARM, &streamparm) < 0)
        return -101;
    //帧率
    streamparm.parm.capture.timeperframe.numerator = 1;
    streamparm.parm.capture.timeperframe.denominator = 25;
    if(ioctl(fd, VIDIOC_S_PARM, &streamparm) < 0)
        return -102;

    //java方法
    jmethodID jmethodID1 = (*jniEnv0)->GetStaticMethodID(jniEnv0, jclass0, "onFrame", "(I)Z");

    //数据缓冲区
    jbyte *byteArrayElements = (*jniEnv0)->GetByteArrayElements(jniEnv0, jbyteArray0, 0);
    rgbFrameData = (unsigned char *) byteArrayElements;
    unsigned char *yuyvFrame = rgbFrameData + width * height * 2;

    debug_addShow(jniEnv0, 20, "    STREAMON")
    //开始视频流
    if(ioctl(fd, VIDIOC_STREAMON, &buffer.type) < 0)
        return -9;
    while (1){

        memset(&buffer, 0, sizeof(buffer));
        buffer.type = V4L2_BUF_TYPE_VIDEO_CAPTURE;
        buffer.memory = V4L2_MEMORY_MMAP;

//        debug_addShow(jniEnv0, 10, "    D*")
        //dqbuf
        if(ioctl(fd, VIDIOC_DQBUF, &buffer) < 0)
            return -10;
//        debug_addShow(jniEnv0, 10, "    Q*")

        //处理缓冲中的数据
//        yuyvFieldToYuyvFrame(void0[buffer.index], width, height, yuyvFrame);
//        yuyvFrameToRgbaFrame(yuyvFrame, width, height, rgbFrameData);
//        yuyvFrameToRgbaFrame(void0[buffer.index], width, height, rgbFrameData);//----------------------------------=============-------------============
        memcpy(rgbFrameData, void0[buffer.index], buffer.length);

        if(ioctl(fd, VIDIOC_QBUF, &buffer) < 0)
            return -11;

        //java  onFrame
        (*jniEnv0)->CallStaticBooleanMethod(jniEnv0, jclass0, jmethodID1, buffer.length);
    }
}