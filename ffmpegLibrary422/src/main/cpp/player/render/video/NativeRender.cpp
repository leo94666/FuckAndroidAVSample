//
// Created by liyang on 2022/5/6.
//

#include "NativeRender.h"


NativeRender::NativeRender(JNIEnv *env, jobject surface)  : VideoRender(VIDEO_RENDER_ANWINDOW){
    m_NativeWindow=ANativeWindow_fromSurface(env,surface);

}

NativeRender::~NativeRender() {
    if(m_NativeWindow)
        ANativeWindow_release(m_NativeWindow);
}

void NativeRender::Init(int videoWidth, int videoHeight, int *dstSize) {
    if(m_NativeWindow == nullptr) return;

    int windowWidth = ANativeWindow_getWidth(m_NativeWindow);
    int windowHeight = ANativeWindow_getHeight(m_NativeWindow);

    if (windowWidth < windowHeight * videoWidth / videoHeight) {
        m_DstWidth = windowWidth;
        m_DstHeight = windowWidth * videoHeight / videoWidth;
    } else {
        m_DstWidth = windowHeight * videoWidth / videoHeight;
        m_DstHeight = windowHeight;
    }
    ANativeWindow_setBuffersGeometry(m_NativeWindow, m_DstWidth,
                                     m_DstHeight, WINDOW_FORMAT_RGBA_8888);

    dstSize[0] = m_DstWidth;
    dstSize[1] = m_DstHeight;
}

void NativeRender::RenderVideoFrame(NativeImage *pImage) {
    if(m_NativeWindow == nullptr || pImage == nullptr) return;
    ANativeWindow_lock(m_NativeWindow, &m_NativeWindowBuffer, nullptr);
    uint8_t *dstBuffer = static_cast<uint8_t *>(m_NativeWindowBuffer.bits);

    int srcLineSize = pImage->width * 4;//RGBA
    int dstLineSize = m_NativeWindowBuffer.stride * 4;

    for (int i = 0; i < m_DstHeight; ++i) {
        memcpy(dstBuffer + i * dstLineSize, pImage->ppPlane[0] + i * srcLineSize, srcLineSize);
    }

    ANativeWindow_unlockAndPost(m_NativeWindow);
}

void NativeRender::UnInit() {

}


