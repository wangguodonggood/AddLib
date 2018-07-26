#ifndef TOPOTEKCAMERA_DEBUGTOOLS_H
#define TOPOTEKCAMERA_DEBUGTOOLS_H

#include "../module/project/debug/logUtils.h"

#include "../module/project/debug/debugHelper.h"
#include "../module/project/debug/debugHelper.h"

void debugTools_show(JNIEnv *jniEnv0, const char *text){
    debugHelper_show(jniEnv0, "com/topotek/topotekmodule/module/debug/NativeDebug", text);
}

void debugTools_addShow(JNIEnv *jniEnv0, const char *text){
    debugHelper_addShow(jniEnv0, "com/topotek/topotekmodule/module/debug/NativeDebug", text);
}

#include <stdio.h>

#define debug_show(z, y, ...) {char text[y];sprintf(text, __VA_ARGS__);debugTools_show(z, text);}
#define debug_addShow(z, y, ...) {char text[y];sprintf(text, __VA_ARGS__);debugTools_addShow(z, text);}

#endif //TOPOTEKCAMERA_DEBUGTOOLS_H
