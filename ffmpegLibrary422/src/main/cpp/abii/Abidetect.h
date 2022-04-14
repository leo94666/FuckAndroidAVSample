//
// Created by liyang on 2022/4/14.
//

#ifndef AV_ABIDETECT_H
#define AV_ABIDETECT_H

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


JNIEXPORT jstring JNICALL getNativeAbi(JNIEnv *env, jclass object) {
#ifdef FFMPEG_KIT_ARM_V7A
    return (*env)->NewStringUTF(env, "arm-v7a");
#elif FFMPEG_KIT_ARM64_V8A
    return (*env)->NewStringUTF(env, "arm64-v8a");
#elif FFMPEG_KIT_X86
    return (*env)->NewStringUTF(env, "x86");
#elif FFMPEG_KIT_X86_64
    return (*env)->NewStringUTF(env, "x86_64");
#else
    return (*env)->NewStringUTF(env, "unknown");
#endif
}

JNIEXPORT jstring JNICALL getNativeCpuAbi(JNIEnv *env, jclass object) {
    AndroidCpuFamily family = android_getCpuFamily();

    if (family == ANDROID_CPU_FAMILY_ARM) {
        uint64_t features = android_getCpuFeatures();

        if (features & ANDROID_CPU_ARM_FEATURE_ARMv7) {
            if (features & ANDROID_CPU_ARM_FEATURE_NEON) {
                return (*env)->NewStringUTF(env, ABI_ARMV7A_NEON);
            } else {
                return (*env)->NewStringUTF(env, ABI_ARMV7A);
            }
        } else {
            return (*env)->NewStringUTF(env, ABI_ARM);
        }

    } else if (family == ANDROID_CPU_FAMILY_ARM64) {
        return (*env)->NewStringUTF(env, ABI_ARM64_V8A);
    } else if (family == ANDROID_CPU_FAMILY_X86) {
        return (*env)->NewStringUTF(env, ABI_X86);
    } else if (family == ANDROID_CPU_FAMILY_X86_64) {
        return (*env)->NewStringUTF(env, ABI_X86_64);
    } else {
        return (*env)->NewStringUTF(env, ABI_UNKNOWN);
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
    return (*env)->NewStringUTF(env, FFMPEG_CONFIGURATION);
}

/** Full name of the Java class that owns native functions in this file. */

#define NATIVE_ABI_CLASS_NAME "com/top/ffmpeg/ffmpeg/AbiDetect"

/** Prototypes of native functions defined by this file. */
JNINativeMethod abiDetectMethods[] = {
        {"getNativeAbi",       "()Ljava/lang/String;", (void *) getNativeAbi},
        {"getNativeCpuAbi",    "()Ljava/lang/String;", (void *) getNativeCpuAbi},
        {"isNativeLTSBuild",   "()Z",                  (void *) isNativeLTSBuild},
        {"getNativeBuildConf", "()Ljava/lang/String;", (void *) getNativeBuildConf}
};


#endif //AV_ABIDETECT_H
