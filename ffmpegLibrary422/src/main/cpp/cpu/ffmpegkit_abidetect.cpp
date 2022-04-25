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

#include <android_log.h>
#include "ffmpegkit_abidetect.h"

static int RegisterNativeMethods(JNIEnv *env, const char *className,
                                 JNINativeMethod *methods, int methodNum) {
  LOGI("RegisterNativeMethods");
  jclass clazz = env->FindClass(className);
  if (clazz == nullptr) {
    LOGE("RegisterNativeMethods fail. clazz == NULL");
    return JNI_FALSE;
  }
  if (env->RegisterNatives(clazz, methods, methodNum) < 0) {
    LOGE("RegisterNativeMethods fail");
    return JNI_FALSE;
  }
  return JNI_TRUE;
}

static void UnregisterNativeMethods(JNIEnv *env, const char *className) {
  LOGI("UnregisterNativeMethods");
  jclass clazz = env->FindClass(className);
  if (clazz == nullptr) {
    LOGE("UnregisterNativeMethods fail. clazz == NULL");
    return;
  }
  env->UnregisterNatives(clazz);
}

jint JNI_OnLoad(JavaVM *jvm, void *p) {
  jint jniRet = JNI_ERR;
  JNIEnv *env = nullptr;
  if (jvm->GetEnv((void **)(&env), JNI_VERSION_1_6) != JNI_OK) {
    return jniRet;
  }

  jint regRet = JNI_ERR;
  regRet = RegisterNativeMethods(
      env, NATIVE_ABI_DETECT_CLASS_NAME, abiDetectMethods,
      sizeof(abiDetectMethods) / sizeof(abiDetectMethods[0]));
  if (regRet != JNI_TRUE) {
    return JNI_ERR;
  }

  return JNI_VERSION_1_6;
}

void JNI_OnUnload(JavaVM *jvm, void *p) {
  JNIEnv *env = nullptr;
  if (jvm->GetEnv((void **)(&env), JNI_VERSION_1_6) != JNI_OK) {
    return;
  }
  UnregisterNativeMethods(env, NATIVE_ABI_DETECT_CLASS_NAME);
}

/**
 * Returns loaded ABI name.
 *
 * @param env pointer to native method interface
 * @param object reference to the class on which this method is invoked
 * @return loaded ABI name as UTF string
 */
JNIEXPORT jstring JNICALL getNativeAbi(JNIEnv *env, jclass object) {

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
JNIEXPORT jstring JNICALL getNativeCpuAbi(JNIEnv *env, jclass object) {
  AndroidCpuFamily family = android_getCpuFamily();

  if (family == ANDROID_CPU_FAMILY_ARM) {
    uint64_t features = android_getCpuFeatures();

    if (features & ANDROID_CPU_ARM_FEATURE_ARMv7) {
      if (features & ANDROID_CPU_ARM_FEATURE_NEON) {
        return env->NewStringUTF( ABI_ARMV7A_NEON);
      } else {
        return env->NewStringUTF( ABI_ARMV7A);
      }
    } else {
      return env->NewStringUTF( ABI_ARM);
    }

  } else if (family == ANDROID_CPU_FAMILY_ARM64) {
    return env->NewStringUTF( ABI_ARM64_V8A);
  } else if (family == ANDROID_CPU_FAMILY_X86) {
    return env->NewStringUTF( ABI_X86);
  } else if (family == ANDROID_CPU_FAMILY_X86_64) {
    return env->NewStringUTF( ABI_X86_64);
  } else {
    return env->NewStringUTF( ABI_UNKNOWN);
  }
}

/**
 * Returns whether FFmpegKit release is a long term release or not.
 *
 * @param env pointer to native method interface
 * @param object reference to the class on which this method is invoked
 * @return yes or no
 */
JNIEXPORT jboolean JNICALL isNativeLTSBuild(JNIEnv *env, jclass object) {
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
JNIEXPORT jstring JNICALL getNativeBuildConf(JNIEnv *env, jclass object) {
  return env->NewStringUTF( FFMPEG_CONFIGURATION);
}
