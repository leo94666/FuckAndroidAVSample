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

#ifndef FFMPEG_KIT_ABIDETECT_H
#define FFMPEG_KIT_ABIDETECT_H

#include <jni.h>
#include <config.h>
#include <cpu-features.h>

/** Represents armeabi-v7a ABI with NEON support. */
#define ABI_ARMV7A_NEON "armeabi-v7a-neon"

/** Represents armeabi-v7a ABI. */
#define ABI_ARMV7A "armeabi-v7a"

/** Represents armeabi ABI. */
#define ABI_ARM "armeabi"

/** Represents x86 ABI. */
#define ABI_X86 "x86"

/** Represents x86_64 ABI. */
#define ABI_X86_64 "x86_64"

/** Represents arm64-v8a ABI. */
#define ABI_ARM64_V8A "arm64-v8a"

/** Represents not supported ABIs. */
#define ABI_UNKNOWN "unknown"

/** Full name of the Java class that owns native functions in this file. */
#define NATIVE_ABI_DETECT_CLASS_NAME "com/top/ffmpeg/ffmpeg/AbiDetect"

#ifdef __cplusplus
extern "C" {
#endif

/**
 * Returns loaded ABI name.
 *
 * @param env pointer to native method interface
 * @param object reference to the class on which this method is invoked
 * @return loaded ABI name as UTF string
 */
JNIEXPORT jstring JNICALL getNativeAbi(JNIEnv *env, jobject instance) {

#ifdef FFMPEG_KIT_ARM_V7A
    return env->NewStringUTF( "arm-v7a");
#elif FFMPEG_KIT_ARM64_V8A
    return env->NewStringUTF( "arm64-v8a");
#elif FFMPEG_KIT_X86
    return env->NewStringUTF( "x86");
#elif FFMPEG_KIT_X86_64
    return env->NewStringUTF( "x86_64");
#else
    return env->NewStringUTF("unknown");
#endif

}

/**
 * Returns ABI name of the running cpu.
 *
 * @param env pointer to native method interface
 * @param object reference to the class on which this method is invoked
 * @return ABI name of the running cpu as UTF string
 */
JNIEXPORT jstring JNICALL getNativeCpuAbi(JNIEnv *env, jobject instance) {
    AndroidCpuFamily family = android_getCpuFamily();

    if (family == ANDROID_CPU_FAMILY_ARM) {
        uint64_t features = android_getCpuFeatures();

        if (features & ANDROID_CPU_ARM_FEATURE_ARMv7) {
            if (features & ANDROID_CPU_ARM_FEATURE_NEON) {
                return env->NewStringUTF(ABI_ARMV7A_NEON);
            } else {
                return env->NewStringUTF(ABI_ARMV7A);
            }
        } else {
            return env->NewStringUTF(ABI_ARM);
        }

    } else if (family == ANDROID_CPU_FAMILY_ARM64) {
        return env->NewStringUTF(ABI_ARM64_V8A);
    } else if (family == ANDROID_CPU_FAMILY_X86) {
        return env->NewStringUTF(ABI_X86);
    } else if (family == ANDROID_CPU_FAMILY_X86_64) {
        return env->NewStringUTF(ABI_X86_64);
    } else {
        return env->NewStringUTF(ABI_UNKNOWN);
    }
}

/**
 * Returns whether FFmpegKit release is a long term release or not.
 *
 * @param env pointer to native method interface
 * @param object reference to the class on which this method is invoked
 * @return yes or no
 */
JNIEXPORT jboolean JNICALL isNativeLTSBuild(JNIEnv *env, jobject instance) {
#if defined(FFMPEG_KIT_LTS)
    return JNI_TRUE;
#else
    return JNI_FALSE;
#endif
}

/**
 * Returns build configuration for FFmpeg.
 *
 * @param env pointer to native method interface
 * @param object reference to the class on which this method is invoked
 * @return build configuration string
 */
JNIEXPORT jstring JNICALL getNativeBuildConf(JNIEnv *env, jobject instance) {
    return env->NewStringUTF(FFMPEG_CONFIGURATION);
}

#ifdef __cplusplus
}
#endif

/** Prototypes of native functions defined by this file. */
static JNINativeMethod abiDetectMethods[] = {
        {"getNativeAbi",       "()Ljava/lang/String;", (void *) getNativeAbi},
        {"getNativeCpuAbi",    "()Ljava/lang/String;", (void *) getNativeCpuAbi},
        {"isNativeLTSBuild",   "()Z",                  (void *) isNativeLTSBuild},
        {"getNativeBuildConf", "()Ljava/lang/String;", (void *) getNativeBuildConf}
};


#endif /* FFMPEG_KIT_ABIDETECT_H */
