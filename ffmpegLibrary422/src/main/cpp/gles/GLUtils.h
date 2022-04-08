//
// Created by liyang on 2022/4/6.
//

#ifndef AV_GLUTILS_H
#define AV_GLUTILS_H
#include <GLES3/gl3.h>
#include "../logger/logger.h"
#include <malloc.h>


class GLUtils {

public:
    static GLuint LoadShader(GLenum shaderType, const char *pSource);
    static GLuint CreateProgram(const char *pVertexShaderSource, const char *pFragShaderSource, GLuint &vertexShaderHandle, GLuint &fragShaderHandle);
    static GLuint DeleteProgram(GLuint &program);
    static void CheckGLError(const char *pGLOperation);

};


#endif //AV_GLUTILS_H
