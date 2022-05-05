cmake_minimum_required(VERSION 3.4.1)
project("player")

include_directories(../include)
include_directories(./decoder)

add_library(LeoPlayer   # 库名称
        SHARED             # 库类型
        Player.cpp
        FFMediaPlayer.cpp
        decoder/VideoDecoder.cpp
        decoder/DecoderBase.cpp
        )            # 编译进库的源码

target_link_libraries(LeoPlayer
        ffmpeg
        ${log-lib})


