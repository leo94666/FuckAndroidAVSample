cmake_minimum_required(VERSION 3.4.1)
project("ffmpegLibrary422")


include_directories(log)
include_directories(include)
include_directories(ffmpeg)
include_directories(utils)


# 查找日志库
find_library(
        log-lib
        log)

add_subdirectory(utils)

add_subdirectory(cpu)


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
        ${log-lib})

add_subdirectory(gles)

add_subdirectory(player)

