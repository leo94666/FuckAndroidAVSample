//
// Created by liyang on 2022/4/6.
//

#ifndef AV_SUPERGLRENDERCONTEXT_H
#define AV_SUPERGLRENDERCONTEXT_H


#include <stdint.h>
#include "../logger/logger.h"
#include "ImageDef.h"
#include "TriangleSample.h"
#include <GLES3/gl3.h>

class SuperGLRenderContext {

public:
     void SetImageData(int format, int width, int height, uint8_t *data);

     void OnSurfaceCreated();

     void OnSurfaceChanged(int width, int height);

     void OnDrawFrame();

     static SuperGLRenderContext *GetInstance();

     static void DestroyInstance();

    SuperGLRenderContext();

    ~SuperGLRenderContext();

protected:
    static SuperGLRenderContext *m_pContext;
    static int m_ScreenW;
    static int m_ScreenH;
    TriangleSample *triangleSample;
};


#endif //AV_SUPERGLRENDERCONTEXT_H
