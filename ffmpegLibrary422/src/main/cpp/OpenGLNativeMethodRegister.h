//
// Created by liyang on 2022/4/8.
//

#ifndef AV_OPENGLNATIVEMETHODREGISTER_H
#define AV_OPENGLNATIVEMETHODREGISTER_H



#include <jni.h>
#include "logger.h"
#include "gles/SuperGLRenderContext.h"


#define NATIVE_OPENGL_CLASS_NAME "com/top/ffmpeg/gles/SuperNativeRender"


#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT void JNICALL native_OnInit(JNIEnv *env, jobject instance) {
    SuperGLRenderContext::GetInstance();
}


JNIEXPORT void JNICALL native_OnUnInit(JNIEnv *env, jobject instance) {
    SuperGLRenderContext::DestroyInstance();
}


JNIEXPORT void JNICALL
native_SetImageData(JNIEnv *env, jobject instance, jint format, jint width, jint height,
                    jbyteArray imageData) {
    int len = env->GetArrayLength(imageData);
    uint8_t *buf = new uint8_t[len];
    env->GetByteArrayRegion(imageData, 0, len, reinterpret_cast<jbyte *>(buf));
    SuperGLRenderContext::GetInstance()->SetImageData(format, width, height, buf);
    delete[] buf;
    env->DeleteLocalRef(imageData);
}


JNIEXPORT void JNICALL
native_OnSurfaceCreated(JNIEnv
                        *env,
                        jobject instance
) {
    SuperGLRenderContext::GetInstance()->OnSurfaceCreated();
}


JNIEXPORT void JNICALL
native_OnSurfaceChanged(JNIEnv *env, jobject instance, jint width, jint height) {
    SuperGLRenderContext::GetInstance()->OnSurfaceChanged(width, height);
}


JNIEXPORT void JNICALL
native_OnDrawFrame(JNIEnv *env, jobject instance) {
    SuperGLRenderContext::GetInstance()->OnDrawFrame();
}

#ifdef __cplusplus
}
#endif


static JNINativeMethod openGlNativeMethods[] = {
        {"native_OnInit",           "()V",      (void *) (native_OnInit)},
        {"native_OnUnInit",         "()V",      (void *) (native_OnUnInit)},
        {"native_SetImageData",     "(III[B)V", (void *) (native_SetImageData)},
        {"native_OnSurfaceCreated", "()V",      (void *) (native_OnSurfaceCreated)},
        {"native_OnSurfaceChanged", "(II)V",    (void *) (native_OnSurfaceChanged)},
        {"native_OnDrawFrame",      "()V",      (void *) (native_OnDrawFrame)},
};




#endif //AV_OPENGLNATIVEMETHODREGISTER_H
