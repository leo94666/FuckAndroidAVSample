#! /usr/bin/env bash
#
#--------------------
echo "do-detect-env.sh================================================================start"

set -e

UNAME_S=$(uname -s)
UNAME_SM=$(uname -sm)
echo "build on $UNAME_SM"

echo "ANDROID_NDK=$ANDROID_NDK"

if [ -z "$ANDROID_NDK" ]; then
    echo "You must define ANDROID_NDK before starting."
    echo "They must point to your NDK directories."
    echo ""
    exit 1
fi



# try to detect NDK version
export IJK_GCC_VER=4.9   ## GCC 版本
export IJK_GCC_64_VER=4.9  ## 64位 GCC 版本
export IJK_MAKE_TOOLCHAIN_FLAGS=
export IJK_MAKE_FLAG=

export IJK_NDK_REL=$(grep -o '^r[0-9]*.*' $ANDROID_NDK/RELEASE.TXT 2>/dev/null | sed 's/[[:space:]]*//g' | cut -b2-) # 获取NDK版本

case "$IJK_NDK_REL" in
    10e*)
        if test -d ${ANDROID_NDK}/toolchains/arm-linux-androideabi-4.8   #文件存在并且是目录
        then
            echo "NDKr$IJK_NDK_REL detected"

            case "$UNAME_S" in
                Darwin)
                    export IJK_MAKE_TOOLCHAIN_FLAGS="$IJK_MAKE_TOOLCHAIN_FLAGS --system=darwin-x86_64"
                ;;
                CYGWIN_NT-*)
                    export IJK_MAKE_TOOLCHAIN_FLAGS="$IJK_MAKE_TOOLCHAIN_FLAGS --system=windows-x86_64"
                ;;
            esac
        else
            echo "You need the NDKr10e or later, 1"
            exit 1
        fi
    ;;
    *)
        IJK_NDK_REL=$(grep -o '^Pkg\.Revision.*=[0-9]*.*' $ANDROID_NDK/source.properties 2>/dev/null | sed 's/[[:space:]]*//g' | cut -d "=" -f 2)
        echo "IJK_NDK_REL=$IJK_NDK_REL"
        case "$IJK_NDK_REL" in
            11*|12*|13*|14*|18*)
                if test -d ${ANDROID_NDK}/toolchains/arm-linux-androideabi-4.9  #文件存在并且是目录
                then
                    echo "NDKr$IJK_NDK_REL detected"
                else
                    echo "You need the NDKr10e or later, 2"
                    exit 1
                fi
            ;;
            23*|24*)
                if test -d ${ANDROID_NDK}/toolchains/llvm
                then
                    echo "NDKr$IJK_NDK_REL detected"
                else
                    echo "You need the NDKr10e or later, 3"
                    exit 1
                fi
            ;;
            *)
                echo "You need the NDKr10e or later, 4"
                exit 1
            ;;
        esac
    ;;
esac


case "$UNAME_S" in
    Darwin)
        export IJK_MAKE_FLAG=-j`sysctl -n machdep.cpu.thread_count`
    ;;
    CYGWIN_NT-*)
        IJK_WIN_TEMP="$(cygpath -am /tmp)"
        export TEMPDIR=$IJK_WIN_TEMP/

        echo "Cygwin temp prefix=$IJK_WIN_TEMP/"
    ;;
esac


echo "IJK_GCC_VER ${IJK_GCC_VER}"
echo "IJK_GCC_64_VER ${IJK_GCC_64_VER}"
echo "IJK_MAKE_TOOLCHAIN_FLAGS ${IJK_MAKE_TOOLCHAIN_FLAGS}"
echo "IJK_MAKE_FLAG ${IJK_MAKE_FLAG}"
echo "IJK_NDK_REL ${IJK_NDK_REL}"


echo "do-detect-env.sh================================================================end"
