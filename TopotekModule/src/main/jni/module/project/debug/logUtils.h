#ifndef TOPOTEKCAMERA_LOGUTILS_H
#define TOPOTEKCAMERA_LOGUTILS_H

#include <android/log.h>

#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR  , "LOGE", __VA_ARGS__)

#endif //TOPOTEKCAMERA_LOGUTILS_H
