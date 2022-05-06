//
// Created by liyang on 2022/5/6.
//

#ifndef AV_PLAYERNATIVEMETHODREGISTER_H
#define AV_PLAYERNATIVEMETHODREGISTER_H


#include <jni.h>

#define NATIVE_OPENGL_CLASS_NAME "com/top/ffmpeg/gles/SuperNativeRender"

#ifdef __cplusplus
extern "C" {
#endif


#ifdef __cplusplus
}
#endif

//static JNINativeMethod playerNativeMethods[] = {
//        {"native_Init",           "()V",      (void *) (native_Init)},
//        {"native_Play",         "()V",      (void *) (native_Play)},
//        {"native_SeekToPosition",     "(III[B)V", (void *) (native_SeekToPosition)},
//        {"native_Pause", "()V",      (void *) (native_Pause)},
//        {"native_OnSurfaceChanged", "(II)V",    (void *) (native_Stop)},
//        {"native_UnInit",      "()V",      (void *) (native_UnInit)},
//        {"native_GetMediaParams",      "()V",      (void *) (native_GetMediaParams)},
//        {"native_SetMediaParams",      "()V",      (void *) (native_SetMediaParams)},
//        {"native_OnSurfaceCreated",      "()V",      (void *) (native_OnSurfaceCreated)},
//        {"native_OnSurfaceChanged",      "()V",      (void *) (native_OnSurfaceChanged)},
//        {"native_OnDrawFrame",      "()V",      (void *) (native_OnDrawFrame)},
//        {"native_SetGesture",      "()V",      (void *) (native_SetGesture)},
//        {"native_SetTouchLoc",      "()V",      (void *) (native_SetTouchLoc)}
//};

#endif //AV_PLAYERNATIVEMETHODREGISTER_H
