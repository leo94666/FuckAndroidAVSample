//
// Created by liyang on 2022/4/8.
//

#ifndef AV_FFMPEGNATIVEMETHODREGISTER_H
#define AV_FFMPEGNATIVEMETHODREGISTER_H


#include <jni.h>
#include <libavutil/ffversion.h>

#define NATIVE_FFMPEG_CLASS_NAME "com/top/ffmpeg/ffmpeg/FFmpegNative"




JNIEXPORT void JNICALL enableNativeRedirection(JNIEnv *env, jobject instance) {

}

JNIEXPORT void JNICALL disableNativeRedirection(JNIEnv *env, jobject instance) {

}

JNIEXPORT void JNICALL setNativeLogLevel(JNIEnv *env, jobject instance) {

}

JNIEXPORT void JNICALL getNativeLogLevel(JNIEnv *env, jobject instance) {

}

JNIEXPORT jstring JNICALL getNativeFFmpegVersion(JNIEnv *env, jobject instance) {
    return env->NewStringUTF(FFMPEG_VERSION);
}

JNIEXPORT void JNICALL getNativeVersion(JNIEnv *env, jobject instance) {

}

JNIEXPORT void JNICALL nativeFFmpegExecute(JNIEnv *env, jobject instance) {

}

JNIEXPORT void JNICALL nativeFFmpegCancel(JNIEnv *env, jobject instance) {

}

JNIEXPORT void JNICALL nativeFFprobeExecute(JNIEnv *env, jobject instance) {

}

JNIEXPORT void JNICALL registerNewNativeFFmpegPipe(JNIEnv *env, jobject instance) {

}

JNIEXPORT void JNICALL getNativeBuildDate(JNIEnv *env, jobject instance) {

}

JNIEXPORT void JNICALL setNativeEnvironmentVariable(JNIEnv *env, jobject instance) {

}

JNIEXPORT void JNICALL ignoreNativeSignal(JNIEnv *env, jobject instance) {

}

JNIEXPORT void JNICALL messagesInTransmit(JNIEnv *env, jobject instance) {

}




/** Prototypes of native functions defined by Config class. */
JNINativeMethod ffmpegNativeMethods[] = {
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


#endif //AV_FFMPEGNATIVEMETHODREGISTER_H
