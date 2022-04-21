//
// Created by liyang on 2022/4/14.
//

#ifndef AV_ABIDETECT_H
#define AV_ABIDETECT_H

#include <config.h>
#include "cpu-features.h"


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

#define NATIVE_ABI_DETECT_CLASS_NAME "com/top/ffmpeg/gles/SuperNativeRender"


#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT jstring JNICALL getNativeAbi(JNIEnv *env, jclass object) {
#ifdef FFMPEG_KIT_ARM_V7A
    return env->NewStringUTF("arm-v7a");
#elif FFMPEG_KIT_ARM64_V8A
    return env->NewStringUTF("arm64-v8a");
#elif FFMPEG_KIT_X86
    return env->NewStringUTF("x86");
#elif FFMPEG_KIT_X86_64
    return env->NewStringUTF( "x86_64");
#else
    return env->NewStringUTF("unknown");
#endif
}

JNIEXPORT jstring JNICALL getNativeCpuAbi(JNIEnv *env, jclass object) {
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

JNIEXPORT jboolean JNICALL isNativeLTSBuild(JNIEnv *env, jclass object) {
#if defined(FFMPEG_KIT_LTS)
    return JNI_TRUE;
#else
    return JNI_FALSE;
#endif
}

JNIEXPORT jstring JNICALL getNativeBuildConf(JNIEnv *env, jclass object) {
    return env->NewStringUTF(FFMPEG_CONFIGURATION);
}

#ifdef __cplusplus
}
#endif

JNINativeMethod abiDetectMethods[] = {
        {"getNativeAbi",       "()Ljava/lang/String;", (void *) getNativeAbi},
        {"getNativeCpuAbi",    "()Ljava/lang/String;", (void *) getNativeCpuAbi},
        {"isNativeLTSBuild",   "()Z",                  (void *) isNativeLTSBuild},
        {"getNativeBuildConf", "()Ljava/lang/String;", (void *) getNativeBuildConf}
};


#endif //AV_ABIDETECT_H
