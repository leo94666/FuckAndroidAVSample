cmake_minimum_required(VERSION 3.4.1)
project("utils")

include_directories(./)

add_library(LeoUtils   # 库名称
        SHARED             # 库类型
        TimeUtils.cpp
        )            # 编译进库的源码

target_link_libraries(LeoUtils
        ${log-lib})


