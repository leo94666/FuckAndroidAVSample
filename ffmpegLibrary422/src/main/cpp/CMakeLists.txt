cmake_minimum_required(VERSION 3.4.1)
project("ffmpegLibrary422")


include_directories(include/)
include_directories(logger/)

# 设置变量
# CMAKE_CXX_FLAGS 表示会将 C++ 的参数传给编译器
# CMAKE_C_FLAGS 表示会将 C 参数传给编译器
# 参数设置 : 传递 CMAKE_CXX_FLAGS C+= 参数给编译器时 , 在 该参数后面指定库的路径
#   CMAKE_SOURCE_DIR 指的是当前的文件地址
#   -L 参数指定动态库的查找路径
#set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -L${CMAKE_SOURCE_DIR}/../jniLibs/${ANDROID_ABI}")

add_library(freetype
        SHARED
        IMPORTED)
set_target_properties(freetype
        PROPERTIES IMPORTED_LOCATION
        ../../../../libs/${ANDROID_ABI}/libfreetype.so)

add_library(avdevice
        SHARED
        IMPORTED)
set_target_properties(avdevice
        PROPERTIES IMPORTED_LOCATION
        ../../../../libs/${ANDROID_ABI}/libavdevice.so)

add_library(avcodec
        SHARED
        IMPORTED)
set_target_properties(avcodec
        PROPERTIES IMPORTED_LOCATION
        ../../../../libs/${ANDROID_ABI}/libavcodec.so)

add_library(avfilter
        SHARED
        IMPORTED)
set_target_properties(avfilter
        PROPERTIES IMPORTED_LOCATION
        ../../../../libs/${ANDROID_ABI}/libavfilter.so)

add_library(avformat
        SHARED
        IMPORTED)
set_target_properties(avformat
        PROPERTIES IMPORTED_LOCATION
        ../../../../libs/${ANDROID_ABI}/libavformat.so)

add_library(avutil
        SHARED
        IMPORTED)
set_target_properties(avutil
        PROPERTIES IMPORTED_LOCATION
        ../../../../libs/${ANDROID_ABI}/libavutil.so)

add_library(swresample
        SHARED
        IMPORTED)
set_target_properties(swresample
        PROPERTIES IMPORTED_LOCATION
        ../../../../libs/${ANDROID_ABI}/libswresample.so)


add_library(avresample
        SHARED
        IMPORTED)
set_target_properties(avresample
        PROPERTIES IMPORTED_LOCATION
        ../../../../libs/${ANDROID_ABI}/libavresample.so)

add_library(swscale
        SHARED
        IMPORTED)

set_target_properties(swscale
        PROPERTIES IMPORTED_LOCATION
        ../../../../libs/${ANDROID_ABI}/libswscale.so)
add_library(postproc
        SHARED
        IMPORTED)
set_target_properties(postproc
        PROPERTIES IMPORTED_LOCATION
        ../../../../libs/${ANDROID_ABI}/libpostproc.so)

message(WARNING "CMAKE_SOURCE_DIR : ${CMAKE_SOURCE_DIR}, ANDROID_ABI : ${ANDROID_ABI}")

#set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=gnu++11")

# 编译一个ffmpeg-cmd库
add_library(FFmpegKits   # 库名称
        SHARED             # 库类型
        ffmpegkits/FFmpegKits.c
        ffmpeg/ffmpeg_info.c
        ffmpegkits/ffmpeg_thread.c
        fftools/cmdutils.c
        fftools/ffmpeg.c
        fftools/ffmpeg_filter.c
        fftools/ffmpeg_opt.c
        fftools/ffmpeg_hw.c
        )            # 编译进库的源码


#add_library(logger   # 库名称
#        SHARED             # 库类型
#        logger/logger.cpp)            # 编译进库的源码

# 查找日志库
find_library(
        log-lib
        log)


target_link_libraries( # Specifies the target library.
        FFmpegKits
        #下面这些是链接的库名称（其实就是so文件去掉前面的lib和后面的.so）
        avcodec
        avdevice
        avfilter
        avformat
        avutil
        avresample
        swresample
        swscale
        postproc
        freetype
        ${log-lib})


#add_library(cpufeatures STATIC ${ANDROID_NDK}/sources/android/cpufeatures/cpu-features.c)
#target_include_directories(hello-neon PRIVATE ${ANDROID_NDK}/sources/android/cpufeatures)
#target_link_libraries(hello-neon cpufeatures)

#target_link_libraries(
#        logger
#        ${log-lib}
#)


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
        SuperNativeRender
        SHARED
        gles/SuperGLRenderContext.cpp
        gles/TriangleSample.cpp
        gles/GLUtils.cpp
        NativeMethodRegister.cpp
)
#
target_link_libraries(
        SuperNativeRender
        -landroid
        -lEGL
        -lGLESv3
        avcodec
        avdevice
        avfilter
        avformat
        avutil
        avresample
        swresample
        swscale
        postproc
        freetype
        ${log-lib}
)

