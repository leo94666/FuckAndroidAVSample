//
// Created by liyang on 2022/4/6.
//

#ifndef AV_SUPERGLRENDERCONTEXT_H
#define AV_SUPERGLRENDERCONTEXT_H


#include <stdint.h>
#include "../log/android_log.h"
class SuperGLRenderContext {

public:
    void SetImageData(int format, int width, int height,uint8_t *data);
    void OnSurfaceCreated();
    void OnSurfaceChanged(int width, int height);
    void OnDrawFrame();
    SuperGLRenderContext * GetInstance();
    void DestroyInstance();

    SuperGLRenderContext();
    ~SuperGLRenderContext();

protected:
    static SuperGLRenderContext *m_pContext;
};


#endif //AV_SUPERGLRENDERCONTEXT_H
