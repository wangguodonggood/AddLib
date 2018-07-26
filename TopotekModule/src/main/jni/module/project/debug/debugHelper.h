#ifndef TOPOTEKCAMERA_DEBUGUTILS_H
#define TOPOTEKCAMERA_DEBUGUTILS_H

#include <jni.h>

JavaVM* javaVM0;

jint JNI_OnLoad(JavaVM* javaVM, void* reserved){

    javaVM0 = javaVM;

    return JNI_VERSION_1_6;
}

JNIEnv * getJniEnv(){

    JNIEnv * jniEnv1 = 0;
    jint i = (*javaVM0)->GetEnv(javaVM0, (void **)&jniEnv1, JNI_VERSION_1_6);
    if(i < 0){
        (*javaVM0)->AttachCurrentThread(javaVM0, &jniEnv1, 0);
//        (*javaVM0)->DetachCurrentThread(javaVM0);//脱离(释放)当前线程
    }
    return jniEnv1;
}

void callJavaStaticTextMethod(JNIEnv *jniEnv0, const char *classAbsolutePathName, const char *methodName, const char *text){
    jclass jclass1 = (*jniEnv0)->FindClass(jniEnv0, classAbsolutePathName);
    jmethodID jmethodID1 = (*jniEnv0)->GetStaticMethodID(jniEnv0, jclass1, methodName, "(Ljava/lang/String;)V");
    jstring jstring1 = (*jniEnv0)->NewStringUTF(jniEnv0, text);
    (*jniEnv0)->CallStaticVoidMethod(jniEnv0, jclass1, jmethodID1, jstring1);
}

void debugHelper_show(JNIEnv *jniEnv0, const char *classAbsolutePathName, const char *text){
    callJavaStaticTextMethod(jniEnv0, classAbsolutePathName, "show", text);
}

void debugHelper_addShow(JNIEnv *jniEnv0, const char *classAbsolutePathName, const char *text){
    callJavaStaticTextMethod(jniEnv0, classAbsolutePathName, "addShow", text);
}

#endif //TOPOTEKCAMERA_DEBUGUTILS_H
