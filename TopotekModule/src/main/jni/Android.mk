LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_LDLIBS:=-L$(SYSROOT)/usr/lib -llog

LOCAL_MODULE    := jni
LOCAL_SRC_FILES := UsbCamera.c module/project/avilib/avilib.c

include $(BUILD_SHARED_LIBRARY)
