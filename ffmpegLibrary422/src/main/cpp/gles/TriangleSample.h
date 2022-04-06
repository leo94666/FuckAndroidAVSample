//
// Created by liyang on 2022/4/6.
//

#ifndef AV_TRIANGLESAMPLE_H
#define AV_TRIANGLESAMPLE_H


#include <GLES3/gl3.h>

class TriangleSample {

public:
    TriangleSample();
    ~TriangleSample();
    void Init();
    void Draw();

    GLuint m_ProgramObj{};

protected:
    GLuint m_VertexShader;
    GLuint m_FragmentShader;
};


#endif //AV_TRIANGLESAMPLE_H
