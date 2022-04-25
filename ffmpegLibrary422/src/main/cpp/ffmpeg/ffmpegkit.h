/*
 * Copyright (c) 2018-2021 Taner Sener
 *
 * This file is part of FFmpegKit.
 *
 * FFmpegKit is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FFmpegKit is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FFmpegKit.  If not, see <http://www.gnu.org/licenses/>.
 */

#ifndef FFMPEG_KIT_H
#define FFMPEG_KIT_H

#include <jni.h>
#include <android/log.h>
#include "libavutil/log.h"
#include "libavutil/ffversion.h"
#include "ffprobekit.h"

/** Library version string */
#define FFMPEG_KIT_VERSION "4.5.1"

/** Defines tag used for Android logging. */
#define LIB_NAME "ffmpeg-kit"

#ifdef __cplusplus
extern "C" {
#endif
JNIEXPORT void JNICALL enableNativeRedirection(JNIEnv *, jclass);

JNIEXPORT void JNICALL disableNativeRedirection(JNIEnv *, jclass);

JNIEXPORT void JNICALL setNativeLogLevel(JNIEnv *, jclass, jint);

JNIEXPORT jint JNICALL getNativeLogLevel(JNIEnv *, jclass);

JNIEXPORT jstring JNICALL getNativeFFmpegVersion(JNIEnv *, jclass);

JNIEXPORT jstring JNICALL getNativeVersion(JNIEnv *, jclass);

JNIEXPORT jint JNICALL nativeFFmpegExecute(JNIEnv *, jclass, jlong, jobjectArray);


JNIEXPORT void JNICALL nativeFFmpegCancel(JNIEnv *, jclass, jlong);

JNIEXPORT int JNICALL registerNewNativeFFmpegPipe(JNIEnv *env, jclass object, jstring ffmpegPipePath);

JNIEXPORT jstring JNICALL getNativeBuildDate(JNIEnv *env, jclass object);

JNIEXPORT int JNICALL setNativeEnvironmentVariable(JNIEnv *env, jclass object, jstring variableName,jstring variableValue);

JNIEXPORT void JNICALL ignoreNativeSignal(JNIEnv *env, jclass object, jint signum);

JNIEXPORT int JNICALL messagesInTransmit(JNIEnv *env, jclass object, jlong id);

#ifdef __cplusplus
}
#endif

/** Full name of the Config class */
const char *configClassName = "com/top/ffmpeg/ffmpeg/FFmpegKitConfig";
//#define NATIVE_FFMPEG_CLASS_NAME "com/top/ffmpeg/ffmpeg/FFmpegKitConfig"

/** Prototypes of native functions defined by Config class. */
static JNINativeMethod ffmpegMethods[] = {
        {"enableNativeRedirection",      "()V",                                     (void *) enableNativeRedirection},
        {"disableNativeRedirection",     "()V",                                     (void *) disableNativeRedirection},
        {"setNativeLogLevel",            "(I)V",                                    (void *) setNativeLogLevel},
        {"getNativeLogLevel",            "()I",                                     (void *) getNativeLogLevel},
        {"getNativeFFmpegVersion",       "()Ljava/lang/String;",                    (void *) getNativeFFmpegVersion},
        {"getNativeVersion",             "()Ljava/lang/String;",                    (void *) getNativeVersion},
        {"nativeFFmpegExecute",          "(J[Ljava/lang/String;)I",                 (void *) nativeFFmpegExecute},
        {"nativeFFmpegCancel",           "(J)V",                                    (void *) nativeFFmpegCancel},
        {"nativeFFprobeExecute",         "(J[Ljava/lang/String;)I",                 (void *) nativeFFprobeExecute},
        {"registerNewNativeFFmpegPipe",  "(Ljava/lang/String;)I",                   (void *) registerNewNativeFFmpegPipe},
        {"getNativeBuildDate",           "()Ljava/lang/String;",                    (void *) getNativeBuildDate},
        {"setNativeEnvironmentVariable", "(Ljava/lang/String;Ljava/lang/String;)I", (void *) setNativeEnvironmentVariable},
        {"ignoreNativeSignal",           "(I)V",                                    (void *) ignoreNativeSignal},
        {"messagesInTransmit",           "(J)I",                                    (void *) messagesInTransmit}
};

#endif /* FFMPEG_KIT_H */