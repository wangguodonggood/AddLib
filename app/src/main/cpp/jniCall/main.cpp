#include "movidius.h"
#include "android/log.h"
#include <opencv2/core/core.hpp>
#include <opencv/cv.h>
#include <opencv2/opencv.hpp>

using namespace cv;
using namespace std;

#define TAG "Wgd"
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,TAG ,__VA_ARGS__)

#include "opencv2/opencv.hpp"

void onCommand(JNIEnv * jniEnv0, char * commond){
     //接收java发来的命令 和跟踪类似
     LOGE(" ------------------------->>>>>>>>>>>>>>>onCommand接受到命令");
     getOneFrame(jniEnv0);
     //run();
     resultToJava(jniEnv0,11111);
    Mat image1, image2;
    cvtColor(image1,image2,COLOR_BayerBG2BGR);

}
void onPreviewFrame(JNIEnv * jniEnv0, signed char * byte, int length, int frameWidth, int frameHeight){
      //获取一帧的方法  返回一帧数据 char*
    LOGE(" ------------------------->>>>>>>>>>>>>>>onPreviewFrame获取一帧");
}
