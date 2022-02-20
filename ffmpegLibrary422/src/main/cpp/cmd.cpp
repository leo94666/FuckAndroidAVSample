#include <jni.h>
#include <string>

extern "C" {
#include <libavutil/avutil.h>
}

extern "C" JNIEXPORT   jstring JNICALL
Java_com_top_ffmpeg_Cmd_version(JNIEnv *env, jclass clazz) {
    std::string version = "当前FFmpeg版本: ";
    av_version_info();
    version.append("4.2.2");
    return env->NewStringUTF(version.c_str());
}extern "C"
JNIEXPORT jint JNICALL
Java_com_top_ffmpeg_Cmd_exec(JNIEnv *env, jobject thiz, jint argc, jobjectArray argv) {


}