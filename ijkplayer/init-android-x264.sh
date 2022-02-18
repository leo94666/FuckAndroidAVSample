#! /usr/bin/env bash
#


echo "init-android-x264.sh================================================================start"

IJK_X264_UPSTREAM=https://gitee.com/leo-av/x264.git
IJK_X264_FORK=https://gitee.com/leo-av/x264.git
IJK_X264_LOCAL_REPO=extra/x264

set -e
TOOLS=tools

git --version

echo "== pull ffmpeg base =="
# 下载FFMpeg源码到指定目录
sh $TOOLS/pull-repo-base.sh $IJK_X264_UPSTREAM $IJK_X264_LOCAL_REPO

# 复制ffmpeg源码到不同arm处理器架构的（armv5 armv7 arm64 x86 x86_64）
function pull_fork()
{
    echo "== pull x264 fork  =="
    sh $TOOLS/pull-repo-ref.sh $IJK_X264_FORK android/contrib/x264 ${IJK_X264_LOCAL_REPO}
    cd android/contrib/x264
    cd -
}

pull_fork


echo "init-android-x264.sh================================================================end"



