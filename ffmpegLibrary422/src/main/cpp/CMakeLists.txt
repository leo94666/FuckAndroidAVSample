cmake_minimum_required(VERSION 3.10.2)
project("ffmpegdemo")
#导入头文件（可以让项目找到头文件，这样才能调用函数）
include_directories("${CMAKE_SOURCE_DIR}/include")
#配置动态链接库所在的目录
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -L${CMAKE_SOURCE_DIR}/../jnilibs/${ANDROID_ABI}")
add_library( # Sets the name of the library.
        native-lib
        SHARED
        native-lib.cpp )

find_library( # Sets the name of the path variable.
        log-lib
        log )
target_link_libraries( # Specifies the target library.
        native-lib
        #下面这些是链接的库名称（其实就是so文件去掉前面的lib和后面的.so）
        ffmpeg
        ${log-lib} )