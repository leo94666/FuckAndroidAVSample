#! /usr/bin/env bash
#
echo "init-android.sh================================================================start"

IJK_FFMPEG_UPSTREAM=https://gitee.com/leo-av/FFmpeg.git
IJK_FFMPEG_FORK=https://gitee.com/leo-av/FFmpeg.git
IJK_FFMPEG_LOCAL_REPO=extra/ffmpeg

set -e
TOOLS=tools

git --version

echo "== pull ffmpeg base =="
# 下载FFMpeg源码到指定目录
sh $TOOLS/pull-repo-base.sh $IJK_FFMPEG_UPSTREAM $IJK_FFMPEG_LOCAL_REPO

# 复制ffmpeg源码到不同arm处理器架构的（armv5 armv7 arm64 x86 x86_64）
function pull_fork()
{
    echo "== pull ffmpeg fork $1 =="
    sh $TOOLS/pull-repo-ref.sh $IJK_FFMPEG_FORK android/contrib/ffmpeg-$1 ${IJK_FFMPEG_LOCAL_REPO}
    cd android/contrib/ffmpeg-$1
    cd -
}

#pull_fork "armv5"
pull_fork "armv7a"
#pull_fork "arm64"
#pull_fork "x86"
#pull_fork "x86_64"

./init-config.sh


echo "init-android.sh================================================================end"


