#include "com_topotek_movidius_jni_JNI.h"
//
// Created by topptek on 2018/5/18.
//
#ifndef ADDLIB_CALLBACK_H
#define ADDLIB_CALLBACK_H
#endif //ADDLIB_CALLBACK_H

//给java调用
void onPreviewFrame(JNIEnv *, signed char *, int, int, int);

void onCommand(JNIEnv *, char *);

void Java_com_topotek_movidius_jni_JNI_onPreviewFrame(JNIEnv *jniEnv0, jclass jclass0, jbyteArray frameData, jint frameWidth, jint frameHeight) {
//    jsize length = (*jniEnv0)->GetArrayLength(jniEnv0, frameData);
    jsize length = jniEnv0->GetArrayLength(frameData);
//    jbyte *byteArrayElements = (*jniEnv0)->GetByteArrayElements(jniEnv0, frameData, 0);
    jbyte *byteArrayElements = jniEnv0->GetByteArrayElements(frameData, 0);
    onPreviewFrame(jniEnv0, byteArrayElements, length, frameWidth, frameHeight);
//    (*jniEnv0)->ReleaseByteArrayElements(jniEnv0, frameData, byteArrayElements, 0);
    jniEnv0->ReleaseByteArrayElements(frameData, byteArrayElements, 0);
}

void Java_com_topotek_movidius_jni_JNI_onCommand(JNIEnv *jniEnv0, jclass jclass0, jstring command) {
//    const char *string = (*jniEnv0)->GetStringUTFChars(jniEnv0, command, 0);
    const char *string = jniEnv0->GetStringUTFChars(command, 0);
    onCommand(jniEnv0, (char *) string);
//    (*jniEnv0)->ReleaseStringUTFChars(jniEnv0, command, string);
    jniEnv0->ReleaseStringUTFChars(command, string);
}