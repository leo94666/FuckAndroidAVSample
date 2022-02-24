cmake_minimum_required(VERSION 3.10.2)
project("ffmpegLibrary422")


# 编译一个ffmpeg-cmd库
add_library(cmd   # 库名称
        SHARED             # 库类型
        cmd.c)            # 编译进库的源码


add_library(ffmpeg
        SHARED
        IMPORTED)
set_target_properties(ffmpeg
        PROPERTIES IMPORTED_LOCATION
        ../jnilibs/${ANDROID_ABI}/libffmpeg.so)


set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=gnu++11")

include_directories(include)

# 查找日志库
find_library(
        log-lib
        log)


target_link_libraries( # Specifies the target library.
        cmd
        #下面这些是链接的库名称（其实就是so文件去掉前面的lib和后面的.so）
        ffmpeg
        ${log-lib})