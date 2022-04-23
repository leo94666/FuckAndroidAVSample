cmake_minimum_required(VERSION 3.4.1)
project("ffmpegLibrary422")


include_directories(abii)
include_directories(logger)
include_directories(include)
include_directories(ffmpeg2)

include(AndroidNdkModules)
android_ndk_import_module_cpufeatures()
set(CPUFEATURES_LIBRARIES cpufeatures)

# 查找日志库
find_library(
        log-lib
        log)


add_library(
        ffmpegkit_abidetect
        SHARED
        ${ANDROID_NDK}/sources/android/cpufeatures
        NativeMethodRegister.cpp
)
#
target_link_libraries(
        ffmpegkit_abidetect
        cpufeatures
        ${log-lib}
)

add_library(ffmpeg
        SHARED
        IMPORTED)

set_target_properties(ffmpeg
        PROPERTIES IMPORTED_LOCATION
        ../../../../libs/${ANDROID_ABI}/libffmpeg.so)

# 编译一个ffmpeg-cmd库
add_library(FFmpegKit   # 库名称
        SHARED             # 库类型
        #ffmpegkits/FFmpegKits.c
        #ffmpeg/ffmpeg_info.c
        #ffmpegkits/ffmpeg_thread.c
        ffmpeg2/ffmpegkit.c
        #ffmpeg2/ffprobekit.c
        ffmpeg2/ffmpegkit_exception.c
        ffmpeg2/android_lts_support.c
        fftools/cmdutils.c
        fftools/ffmpeg.c
        #fftools/ffprobe.c
        fftools/ffmpeg_filter.c
        fftools/ffmpeg_opt.c
        fftools/ffmpeg_hw.c
        )            # 编译进库的源码

target_link_libraries( # Specifies the target library.
        FFmpegKit
        #下面这些是链接的库名称（其实就是so文件去掉前面的lib和后面的.so）
        ffmpegkit_abidetect
        ffmpeg
        cpufeatures
        ${log-lib})


#OpenGles
# 如果当前系统版本过低设置拒绝编译
#if (${ANDROID_PLATFORM_LEVEL} LESS 11)
#    message(FATAL_ERROR "OPENGL ES2 is not support before API level 11")
#    return()
#elseif (${ANDROID_PLATFORM_LEVEL} LESS 18)
#    add_definitions("-DDYNAMIC_ES3")
#    set(OPENGL_LIB GLESv2)
#else ()
#    set(OPENGL_LIB GLESv3)
#endif (${ANDROID_PLATFORM_LEVEL} LESS 11)
#message($OPENGL_LIB)
#
#add_library(
#        OpenGL
#        SHARED
#        gles/SuperGLRenderContext.cpp
#        gles/TriangleSample.cpp
#        gles/GLUtils.cpp
#)
##
#target_link_libraries(
#        OpenGL
#        -landroid
#        -lEGL
#        -lGLESv3
#        ${log-lib}
#)


####################

#add_library(
#        NativeMethodRegister
#        SHARED
#        NativeMethodRegister.cpp
#)
##
#target_link_libraries(
#        NativeMethodRegister
#        FFmpegKit
#        ffmpegkit_abidetect
#        ffmpeg
#        cpufeatures
#        ${log-lib}
#)
