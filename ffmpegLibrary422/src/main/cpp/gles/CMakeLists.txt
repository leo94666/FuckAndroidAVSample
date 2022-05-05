cmake_minimum_required(VERSION 3.4.1)
project("opengles")




#OpenGles
# 如果当前系统版本过低设置拒绝编译
if (${ANDROID_PLATFORM_LEVEL} LESS 11)
    message(FATAL_ERROR "OPENGL ES2 is not support before API level 11")
    return()
elseif (${ANDROID_PLATFORM_LEVEL} LESS 18)
    add_definitions("-DDYNAMIC_ES3")
    set(OPENGL_LIB GLESv2)
else ()
    set(OPENGL_LIB GLESv3)
endif (${ANDROID_PLATFORM_LEVEL} LESS 11)
message($OPENGL_LIB)

add_library(
        OpenGL
        SHARED
        OpenGLNativeMethodRegister.cpp
        SuperGLRenderContext.cpp
        TriangleSample.cpp
        GLUtils.cpp
)
#
target_link_libraries(
        OpenGL
        -landroid
        -lEGL
        -lGLESv3
        ${log-lib}
)



