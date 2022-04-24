//
// Created by liyang on 2022/3/6.
//

#ifndef AV_FFMPEG_INFO_H
#define AV_FFMPEG_INFO_H


#include "jni.h"

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT jstring JNICALL Java_com_top_ffmpeg_FFmpegKits_urlProtocolInfo(JNIEnv *env, jclass type);

JNIEXPORT jstring JNICALL Java_com_top_ffmpeg_FFmpegKits_avFilterInfo(JNIEnv *env, jclass type);




#ifdef __cplusplus
}
#endif
#endif //AV_FFMPEG_INFO_H
