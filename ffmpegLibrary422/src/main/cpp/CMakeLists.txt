cmake_minimum_required(VERSION 3.10.2)
project("ffmpegLibrary422")
#1.导入头文件（可以让项目找到头文件，这样才能调用函数）
include_directories(
        "${CMAKE_SOURCE_DIR}/include"
        "${CMAKE_SOURCE_DIR}/fftools"
)

#2.引入库文件，配置动态链接库所在的目录
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -L${CMAKE_SOURCE_DIR}/../jnilibs/${ANDROID_ABI}")

#
#add_library(ffmpeg
#        SHARED
#        IMPORTED )
#set_target_properties(ffmpeg
#        PROPERTIES IMPORTED_LOCATION
#        ${CMAKE_SOURCE_DIR}/../jnilibs/${ANDROID_ABI}/libffmpeg.so )

# 编译一个ffmpeg-cmd库
add_library(cmd   # 库名称
        SHARED             # 库类型
        cmd.cpp
<<<<<<< HEAD
        ffmpeg.c
        fftools/cmdutils.c
        fftools/ffmpeg_cuvid.c
        fftools/ffmpeg_filter.c
        fftools/ffmpeg_hw.c
        fftools/ffmpeg_opt.c
        fftools/ffmpeg_qsv.c
        fftools/ffmpeg_videotoolbox.c

=======
>>>>>>> 4a0165d387fc71c587747c3b2d119a21674a2b33
        )            # 编译进库的源码

# 查找日志库
find_library(
        log-lib
        log)

target_link_libraries( # Specifies the target library.
        cmd
        #下面这些是链接的库名称（其实就是so文件去掉前面的lib和后面的.so）
        fftools
        ${log-lib})