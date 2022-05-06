//
// Created by liyang on 2022/5/6.
//

#ifndef AV_NATIVERENDER_H
#define AV_NATIVERENDER_H


#include <jni.h>
#include "VideoRender.h"
#include <android/native_window.h>
#include <android/native_window_jni.h>
#include <ImageDef.h>

class NativeRender : public VideoRender {
public:
    NativeRender(JNIEnv *env, jobject surface);

    virtual ~NativeRender();

    virtual void Init(int videoWidth, int videoHeight, int *dstSize);

    virtual void RenderVideoFrame(NativeImage *pImage);

    virtual void UnInit();

private:
    ANativeWindow_Buffer m_NativeWindowBuffer;
    ANativeWindow *m_NativeWindow = nullptr;
    int m_DstWidth;
    int m_DstHeight;
};


#endif //AV_NATIVERENDER_H
