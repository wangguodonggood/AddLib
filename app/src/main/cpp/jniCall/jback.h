//
// Created by wgd on 2018/5/21.
//调用java的方法
//
#include <jni.h>
#ifndef ADDLIB_TO_H
#define ADDLIB_TO_H
//...
void getOneFrame(JNIEnv *jniEnv0){
    jclass jclass1 = jniEnv0->FindClass("com/topotek/movidius/jni/JNI");
//    jmethodID jmethodID_getOneFrame = (*jniEnv0)->GetStaticMethodID(jniEnv0, jclass1, "getOneFrame", "()V");
    jmethodID jmethodID_getOneFrame = jniEnv0->GetStaticMethodID(jclass1, "getOneFrame", "()V");
//    (*jniEnv0)->CallStaticVoidMethod(jniEnv0, jclass1, jmethodID_getOneFrame);
    jniEnv0->CallStaticVoidMethod(jclass1, jmethodID_getOneFrame);
}

void resultToJava(JNIEnv *jniEnv0,int res){
    jclass jclass1 = jniEnv0->FindClass("com/topotek/movidius/jni/JNI");
    jmethodID jmethodID_resultToJava=jniEnv0->GetStaticMethodID(jclass1, "resultToJava", "(I)V");
    jniEnv0->CallStaticVoidMethod(jclass1, jmethodID_resultToJava);
}
#endif //ADDLIB_TO_H




