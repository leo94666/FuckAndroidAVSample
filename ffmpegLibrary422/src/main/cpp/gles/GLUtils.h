//
// Created by liyang on 2022/4/6.
//

#ifndef AV_GLUTILS_H
#define AV_GLUTILS_H
#include <GLES3/gl3.h>


class GLUtils {

public:
    GLuint LoadShader(GLenum shaderType, const char *pSource);
    static GLuint CreateProgram(const char *pVertexShaderSource, const char *pFragShaderSource, GLuint &vertexShaderHandle, GLuint &fragShaderHandle);
    GLuint DeleteProgram(GLuint &program);
    void CheckGLError(const char *pGLOperation);

};


#endif //AV_GLUTILS_H
