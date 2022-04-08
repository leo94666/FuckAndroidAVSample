//
// Created by liyang on 2022/4/8.
//

#include <jni.h>
#include "logger.h"
#include "gles/SuperGLRenderContext.h"
#include "OpenGLNativeMethodRegister.h"
#include "FFmpegNativeMethodRegister.h"


static int
RegisterNativeMethods(JNIEnv *env, const char *className, JNINativeMethod *methods, int methodNum) {
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

// call this func when loading lib
//JNI_OnLoad函数，构造函数
// 当Android的VM(Virtual Machine)执行到C组件(即*so档)里的System.loadLibrary()函数时，
//首先会去执行C组件里的JNI_OnLoad()函数。
//它的用途有二：
//. 告诉VM此C组件使用那一个JNI版本。
//  如果你的*.so档没有提供JNI_OnLoad()函数，VM会默认该*.so档是使用最老的JNI 1.1版本。
//  由于新版的JNI做了许多扩充，如果需要使用JNI的新版功能，
//  例如JNI 1.4的java.nio.ByteBuffer,就必须藉由JNI_OnLoad()函数来告知VM。
//
//. 由于VM执行到System.loadLibrary()函数时，就会立即先呼叫JNI_OnLoad()，
//  所以C组件的开发者可以藉由JNI_OnLoad()来进行C组件内的初期值之设定(Initialization) 。
JNIEXPORT jint JNI_OnLoad(JavaVM *jvm, void *p) {
    jint jniRet = JNI_ERR;
    JNIEnv *env = nullptr;
    if (jvm->GetEnv((void **) (&env), JNI_VERSION_1_6) != JNI_OK) {
        return jniRet;
    }

    jint regRet = JNI_ERR;
    regRet = RegisterNativeMethods(env, NATIVE_OPENGL_CLASS_NAME, openGlNativeMethods,
                                   sizeof(openGlNativeMethods) /
                                   sizeof(openGlNativeMethods[0]));
    if (regRet != JNI_TRUE) {
        return JNI_ERR;
    }

    regRet = RegisterNativeMethods(env, NATIVE_FFMPEG_CLASS_NAME, ffmpegNativeMethods,
                                   sizeof(ffmpegNativeMethods) / sizeof(ffmpegNativeMethods[0]));
    if (regRet != JNI_TRUE) {
        return JNI_ERR;
    }

    return JNI_VERSION_1_6;
}

JNIEXPORT void JNI_OnUnload(JavaVM *jvm, void *p) {
    JNIEnv *env = NULL;
    if (jvm->GetEnv((void **) (&env), JNI_VERSION_1_6) != JNI_OK) {
        return;
    }
    UnregisterNativeMethods(env, NATIVE_OPENGL_CLASS_NAME);
    UnregisterNativeMethods(env, NATIVE_FFMPEG_CLASS_NAME);
}

