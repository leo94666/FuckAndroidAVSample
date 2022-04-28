cmake_minimum_required(VERSION 3.4.1)
project("ffmpegLibrary422")


include_directories(abii)
include_directories(log)
include_directories(include)
include_directories(cpu)
include_directories(ffmpeg)

#include(AndroidNdkModules)
#android_ndk_import_module_cpufeatures()
#set(CPUFEATURES_LIBRARIES cpufeatures)

# 查找日志库
find_library(
        log-lib
        log)

add_library(cpu_features
        STATIC
        IMPORTED)

set_target_properties(cpu_features
        PROPERTIES IMPORTED_LOCATION
        ../../../../libs/${ANDROID_ABI}/libcpu_features.a)

add_library(ndk_compat
        STATIC
        IMPORTED)

set_target_properties(ndk_compat
        PROPERTIES IMPORTED_LOCATION
        ../../../../libs/${ANDROID_ABI}/libndk_compat.a)

add_library(
        ffmpegkit_abidetect
        SHARED
        cpu/list_cpu_features.c
        cpu/ffmpegkit_abidetect.cpp
        ${ANDROID_NDK}/sources/android/cpufeatures
)
#
target_link_libraries(
        ffmpegkit_abidetect
        cpu_features
        ndk_compat
        ${log-lib}
)

add_library(ffmpeg
        SHARED
        IMPORTED)

set_target_properties(ffmpeg
        PROPERTIES IMPORTED_LOCATION
        ../../../../libs/${ANDROID_ABI}/libffmpeg.so)

# 编译一个ffmpeg-cmd库
add_library(ffmpegkit   # 库名称
        SHARED             # 库类型
        ffmpeg/ffmpegkit.c
        ffmpeg/ffprobekit.c
        ffmpeg/ffmpegkit_exception.c
        fftools/cmdutils.c
        fftools/ffmpeg.c
        fftools/ffprobe.c
        fftools/ffmpeg_opt.c
        fftools/ffmpeg_hw.c
        fftools/ffmpeg_filter.c
        #ffmpeg/android_lts_support.c
        )            # 编译进库的源码

target_link_libraries(ffmpegkit
        #下面这些是链接的库名称（其实就是so文件去掉前面的lib和后面的.so）
        ffmpegkit_abidetect
        ffmpeg
        cpu_features
        ${log-lib})




#OpenGles
# 如果当前系统版本过低设置拒绝编译
if (${ANDROID_PLATFORM_LEVEL} LESS 11)
    message(FATAL_ERROR "OPENGL ES2 is not support before API level 11")
    return()
elseif (${ANDROID_PLATFORM_LEVEL} LESS 18)
    add_definitions("-DDYNAMIC_ES3")
    set(OPENGL_LIB GLESv2)
else ()
    set(OPENGL_LIB GLESv3)
endif (${ANDROID_PLATFORM_LEVEL} LESS 11)
message($OPENGL_LIB)

add_library(
        OpenGL
        SHARED
        gles/OpenGLNativeMethodRegister.cpp
        gles/SuperGLRenderContext.cpp
        gles/TriangleSample.cpp
        gles/GLUtils.cpp
)
#
target_link_libraries(
        OpenGL
        -landroid
        -lEGL
        -lGLESv3
        ${log-lib}
)

