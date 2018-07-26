#include <jni.h>
//
// Created by topptek on 2018/5/18.
//
#ifndef ADDLIB_COM_TOPOTEK_MOVIDIUS_JNI_JNI_H
#define ADDLIB_COM_TOPOTEK_MOVIDIUS_JNI_JNI_H
#endif //ADDLIB_COM_TOPOTEK_MOVIDIUS_JNI_JNI_H
#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT void JNICALL Java_com_topotek_movidius_jni_JNI_onPreviewFrame
        (JNIEnv *, jclass, jbyteArray, jint, jint);

JNIEXPORT void JNICALL Java_com_topotek_movidius_jni_JNI_onCommand
        (JNIEnv *, jclass, jstring);

#ifdef __cplusplus
}
#endif

