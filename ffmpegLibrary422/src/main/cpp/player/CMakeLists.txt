cmake_minimum_required(VERSION 3.4.1)
project("player")

#set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=gnu++11")

include_directories(../include)
include_directories(./decoder)
include_directories(./mediaplayer)

add_library(LeoPlayer   # 库名称
        SHARED             # 库类型
        mediaplayer/FFMediaPlayer.cpp
        Player.cpp
        decoder/DecoderBase.cpp
        decoder/VideoDecoder.cpp
        render/NativeRender.cpp
        PlayerNativeMethodRegister.cpp
        )            # 编译进库的源码

target_link_libraries(LeoPlayer
        ffmpeg
        LeoUtils
        android
        ${log-lib})


