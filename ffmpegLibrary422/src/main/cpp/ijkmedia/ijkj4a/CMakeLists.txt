cmake_minimum_required(VERSION 3.4.1)
project("ijkj4a")

include(j4a)
include(j4au)

# 编译一个ffmpeg-cmd库
add_library(ijkj4a
        SHARED
        j4a/j4a_allclasses.c
        j4a/j4a_base.c
        j4a/class/android/media/AudioTrack.c
        j4a/class/android/media/MediaCodec.c
        j4a/class/android/media/MediaFormat.c
        j4a/class/android/media/PlaybackParams.c
        j4a/class/android/os/Build.c
        j4a/class/android/os/Bundle.c
        j4a/class/java/nio/Buffer.c
        j4a/class/java/nio/ByteBuffer.c
        j4a/class/java/util/ArrayList.c
        j4a/class/tv/danmaku/ijk/media/player/misc/IMediaDataSource.c
        j4a/class/tv/danmaku/ijk/media/player/misc/IAndroidIO.c
        j4a/class/tv/danmaku/ijk/media/player/IjkMediaPlayer.c
        j4au/class/android/media/AudioTrack.util.c
        j4au/class/java/nio/ByteBuffer.util.c
        )
