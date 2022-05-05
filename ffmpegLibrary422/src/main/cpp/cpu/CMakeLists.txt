cmake_minimum_required(VERSION 3.4.1)
project("cpu")

include_directories(./)

add_library(cpu_features
        STATIC
        IMPORTED)

set_target_properties(cpu_features
        PROPERTIES IMPORTED_LOCATION
        ../../../../libs/${ANDROID_ABI}/libcpu_features.a)

add_library(ndk_compat
        STATIC
        IMPORTED)

set_target_properties(ndk_compat
        PROPERTIES IMPORTED_LOCATION
        ../../../../libs/${ANDROID_ABI}/libndk_compat.a)

add_library(
        ffmpegkit_abidetect
        SHARED
        list_cpu_features.c
        ffmpegkit_abidetect.cpp
        ${ANDROID_NDK}/sources/android/cpufeatures
)
#
target_link_libraries(
        ffmpegkit_abidetect
        cpu_features
        ndk_compat
        ${log-lib}
)



