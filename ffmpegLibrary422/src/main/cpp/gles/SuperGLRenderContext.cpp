//
// Created by liyang on 2022/4/6.
//

#include <logger.h>
#include "SuperGLRenderContext.h"

SuperGLRenderContext* SuperGLRenderContext::m_pContext = nullptr;

void SuperGLRenderContext::SetImageData(int format, int width, int height, uint8_t *data) {
    LOGE("SuperGLRenderContext::SetImageData [format=%d, width=%d, height=%d data=%p]", format,
         width, height, data);
    NativeImage nativeImage;
    nativeImage.format = format;
    nativeImage.width = width;
    nativeImage.height = height;
    nativeImage.ppPlane[0] = data;

    switch (format) {
        case IMAGE_FORMAT_NV12:
        case IMAGE_FORMAT_NV21:
            nativeImage.ppPlane[1] = nativeImage.ppPlane[0] + width * height;
            break;
        case IMAGE_FORMAT_I420:
            nativeImage.ppPlane[1] = nativeImage.ppPlane[0] + width * height;
            nativeImage.ppPlane[2] = nativeImage.ppPlane[1] + width * height / 4;
            break;
        default:
            break;
    }

//    if (m_pCurSample)
//    {
//        m_pCurSample->LoadImage(&nativeImage);
//    }

}

void SuperGLRenderContext::OnSurfaceCreated() {
    LOGE("SuperGLRenderContext::OnSurfaceCreated");
    glClearColor(1.0f, 1.0f, 0.5f, 1.0f);
    triangleSample->Init();
}

void SuperGLRenderContext::OnSurfaceChanged(int width, int height) {
    LOGE("SuperGLRenderContext::OnSurfaceChanged [w,h]=[%d, %d]", width, height);
    glViewport(0, 0, width, height);
}

void SuperGLRenderContext::OnDrawFrame() {
    LOGE("SuperGLRenderContext::OnDrawFrame");
    glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
    triangleSample->Draw();
}

SuperGLRenderContext *SuperGLRenderContext::GetInstance() {
    LOGE("SuperGLRenderContext::GetInstance()");
    if (m_pContext == nullptr) {
        m_pContext = new SuperGLRenderContext();
    }
    return m_pContext;
}

void SuperGLRenderContext::DestroyInstance() {
    LOGE("SuperGLRenderContext::DestroyInstance()");
    if (m_pContext) {
        delete m_pContext;
        m_pContext = nullptr;
    }
}

SuperGLRenderContext::SuperGLRenderContext() {
    triangleSample = new TriangleSample();
}

SuperGLRenderContext::~SuperGLRenderContext() {

}



