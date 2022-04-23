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

/*
 * Class:     com_arthenica_ffmpegkit_FFmpegKitConfig
 * Method:    enableNativeRedirection
 * Signature: ()V
 */
JNIEXPORT void JNICALL enableNativeRedirection(JNIEnv *, jclass);

/*
 * Class:     com_arthenica_ffmpegkit_FFmpegKitConfig
 * Method:    disableNativeRedirection
 * Signature: ()V
 */
JNIEXPORT void JNICALL disableNativeRedirection(JNIEnv *, jclass);

/*
 * Class:     com_arthenica_ffmpegkit_FFmpegKitConfig
 * Method:    setNativeLogLevel
 * Signature: (I)V
 */
JNIEXPORT void JNICALL setNativeLogLevel(JNIEnv *, jclass, jint);

/*
 * Class:     com_arthenica_ffmpegkit_FFmpegKitConfig
 * Method:    getNativeLogLevel
 * Signature: ()I
 */
JNIEXPORT jint JNICALL getNativeLogLevel(JNIEnv *, jclass);

/*
 * Class:     com_arthenica_ffmpegkit_FFmpegKitConfig
 * Method:    getNativeFFmpegVersion
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL getNativeFFmpegVersion(JNIEnv *, jclass);

/*
 * Class:     com_arthenica_ffmpegkit_FFmpegKitConfig
 * Method:    getNativeVersion
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL getNativeVersion(JNIEnv *, jclass);

/*
 * Class:     com_arthenica_ffmpegkit_FFmpegKitConfig
 * Method:    nativeFFmpegExecute
 * Signature: (J[Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL nativeFFmpegExecute(JNIEnv *, jclass, jlong, jobjectArray);

/*
 * Class:     com_arthenica_ffmpegkit_FFmpegKitConfig
 * Method:    nativeFFmpegCancel
 * Signature: (J)V
 */
JNIEXPORT void JNICALL nativeFFmpegCancel(JNIEnv *, jclass, jlong);

/*
 * Class:     com_arthenica_ffmpegkit_FFmpegKitConfig
 * Method:    registerNewNativeFFmpegPipe
 * Signature: (Ljava/lang/String;)I
 */
JNIEXPORT int JNICALL
registerNewNativeFFmpegPipe(JNIEnv *env, jclass object, jstring ffmpegPipePath);

/*
 * Class:     com_arthenica_ffmpegkit_FFmpegKitConfig
 * Method:    getNativeBuildDate
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL getNativeBuildDate(JNIEnv *env, jclass object);

/**
 * Class:     com_arthenica_ffmpegkit_FFmpegKitConfig
 * Method:    setNativeEnvironmentVariable
 * Signature: (Ljava/lang/String;Ljava/lang/String;)I
 */
JNIEXPORT int JNICALL setNativeEnvironmentVariable(JNIEnv *env, jclass object, jstring variableName,
                                                   jstring variableValue);

/*
 * Class:     com_arthenica_ffmpegkit_FFmpegKitConfig
 * Method:    ignoreNativeSignal
 * Signature: (I)V
 */
JNIEXPORT void JNICALL ignoreNativeSignal(JNIEnv *env, jclass object, jint signum);

/*
 * Class:     com_arthenica_ffmpegkit_FFmpegKitConfig
 * Method:    messagesInTransmit
 * Signature: (J)I
 */
JNIEXPORT int JNICALL messagesInTransmit(JNIEnv *env, jclass object, jlong id);

#ifdef __cplusplus
}
#endif

/** Full name of the Config class */
//const char *configClassName = "com/top/ffmpeg/ffmpeg/FFmpegKitConfig";
#define NATIVE_FFMPEG_CLASS_NAME "com/top/ffmpeg/ffmpeg/FFmpegKitConfig"

/** Prototypes of native functions defined by Config class. */
JNINativeMethod ffmpegMethods[] = {
        {"enableNativeRedirection",      "()V",                                     (void *) enableNativeRedirection},
        {"disableNativeRedirection",     "()V",                                     (void *) disableNativeRedirection},
        {"setNativeLogLevel",            "(I)V",                                    (void *) setNativeLogLevel},
        {"getNativeLogLevel",            "()I",                                     (void *) getNativeLogLevel},
        {"getNativeFFmpegVersion",       "()Ljava/lang/String;",                    (void *) getNativeFFmpegVersion},
        {"getNativeVersion",             "()Ljava/lang/String;",                    (void *) getNativeVersion},
        {"nativeFFmpegExecute",          "(J[Ljava/lang/String;)I",                 (void *) nativeFFmpegExecute},
        {"nativeFFmpegCancel",           "(J)V",                                    (void *) nativeFFmpegCancel},
        // {"nativeFFprobeExecute",         "(J[Ljava/lang/String;)I",                 (void *) nativeFFprobeExecute},
        {"registerNewNativeFFmpegPipe",  "(Ljava/lang/String;)I",                   (void *) registerNewNativeFFmpegPipe},
        {"getNativeBuildDate",           "()Ljava/lang/String;",                    (void *) getNativeBuildDate},
        {"setNativeEnvironmentVariable", "(Ljava/lang/String;Ljava/lang/String;)I", (void *) setNativeEnvironmentVariable},
        {"ignoreNativeSignal",           "(I)V",                                    (void *) ignoreNativeSignal},
        {"messagesInTransmit",           "(J)I",                                    (void *) messagesInTransmit}
};

#endif /* FFMPEG_KIT_H */