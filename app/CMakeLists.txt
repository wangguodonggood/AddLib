cmake_minimum_required(VERSION 3.4.1)
set(LIBUSB_DIR ${CMAKE_SOURCE_DIR}/libs)

set(CMAKE_VERBOSE_MAKEFILE on)
set(ocvlibs "${CMAKE_SOURCE_DIR}/src/main/jniLibs")
include_directories(${CMAKE_SOURCE_DIR}/src/main/cpp/include)
include_directories(${INC_DIR})

add_library(usb-lib SHARED IMPORTED)
set_target_properties(usb-lib PROPERTIES IMPORTED_LOCATION
             ${LIBUSB_DIR}/${ANDROID_ABI}/libusb1.0.so)

add_library(libopencv_java3 SHARED IMPORTED )
set_target_properties(libopencv_java3 PROPERTIES
                      IMPORTED_LOCATION "${ocvlibs}/${ANDROID_ABI}/libopencv_java3.so")

add_library(
             native-lib
             SHARED
             src/main/cpp/native-lib.cpp
             src/main/cpp/mvnc_api.c
             src/main/cpp/usb_boot.c
             src/main/cpp/usb_link_vsc.c
             src/main/cpp/fp16.c
             src/main/cpp/run.cpp
             src/main/cpp/jniCall/main.cpp
             )

find_library(
              log-lib
              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log )
target_link_libraries(
                       native-lib usb-lib libopencv_java3 libopencv_face
                       libopencv_aruco  libopencv_bgsegm libopencv_bioinspired
                       libopencv_calib3d libopencv_ccalib libopencv_core libopencv_datasets
                       libopencv_dnn  libopencv_dnn_objdetect libopencv_tracking
                       ${log-lib} )