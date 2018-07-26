cmake_minimum_required(VERSION 3.4.1)
add_library(
             topotek
             SHARED
             src/main/jni/UsbCamera.c
             src/main/jni/module/project/avilib/avilib.c
             )
find_library(
              log-lib
              log )
target_link_libraries(
                       topotek
                       ${log-lib} )