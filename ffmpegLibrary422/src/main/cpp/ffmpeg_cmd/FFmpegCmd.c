#include <jni.h>
#include <string.h>
#include <stdio.h>
#include <libavcodec/avcodec.h>
#include "ffmpeg_thread.h"
#include "../log/android_log.h"
#include "com_top_ffmpeg_cmd.h"
#include "../ffmpeg/ffmpeg_info.h"
static JavaVM *jvm = NULL;

static jclass m_clazz = NULL;//当前类(面向java)


/**
 * 回调执行Java方法--onExecuted
 */
void callJavaMethod(JNIEnv *env, jclass clazz, int ret) {
    if (clazz == NULL) {
        LOGE("----------callJavaMethod-----clazz isNULL---------------");
        return;
    }
    //获取方法ID (I)V指的是方法签名 通过javap -s -public FFmpegCmd 命令生成
    jmethodID methodID = (*env)->GetStaticMethodID(env, clazz, "onExecuted", "(I)V");
    if (methodID == NULL) {
        LOGE("---------------methodID isNULL---------------");
        return;
    }
    //调用该java方法
    (*env)->CallStaticVoidMethod(env, clazz, methodID, ret);
}


/**
 * 回调执行Java方法--onProgress
 */
void callJavaMethodProgress(JNIEnv *env, jclass clazz, float ret) {
    if (clazz == NULL) {
        LOGE("----------callJavaMethodProgress-----clazz isNULL---------------");
        return;
    }
    //获取方法ID (I)V指的是方法签名 通过javap -s -public FFmpegCmd 命令生成
    jmethodID methodID = (*env)->GetStaticMethodID(env, clazz, "onProgress", "(F)V");
    if (methodID == NULL) {
        LOGE("---------------methodID isNULL---------------");
        return;
    }
    //调用该java方法
    (*env)->CallStaticVoidMethod(env, clazz, methodID, ret);
}

/**
 * c语言-线程回调
 */
static void ffmpeg_callback(int ret) {
    JNIEnv *env;
    //附加到当前线程从JVM中取出JNIEnv, C/C++从子线程中直接回到Java里的方法时  必须经过这个步骤
    (*jvm)->AttachCurrentThread(jvm, (void **) &env, NULL);
    callJavaMethod(env, m_clazz, ret);
    //完毕-脱离当前线程
    (*jvm)->DetachCurrentThread(jvm);
}

void ffmpeg_progress(float progress) {
    JNIEnv *env;
    (*jvm)->AttachCurrentThread(jvm, (void **) &env, NULL);
    callJavaMethodProgress(env, m_clazz, progress);
    (*jvm)->DetachCurrentThread(jvm);
}

JNIEXPORT jstring JNICALL
Java_com_top_ffmpeg_FFmpegCmd_version(JNIEnv *env, jclass clazz) {
    char str[25];
    sprintf(str, "%d", avcodec_version());
    return (*env)->NewStringUTF(env, str);
}

JNIEXPORT jint JNICALL
Java_com_top_ffmpeg_FFmpegCmd_exec(JNIEnv *env, jclass clazz, jint cmdnum, jobjectArray cmdline) {
    (*env)->GetJavaVM(env, &jvm);
    //创建全局引用
    m_clazz = (*env)->NewGlobalRef(env, clazz);
    //---------------------------------C语言 反射Java 相关----------------------------------------
    //---------------------------------java 数组转C语言数组----------------------------------------
    int i = 0;//满足NDK所需的C99标准
    char **argv = NULL;//命令集 二维指针
    jstring *strr = NULL;

    if (cmdline != NULL) {
        argv = (char **) malloc(sizeof(char *) * cmdnum);
        strr = (jstring *) malloc(sizeof(jstring) * cmdnum);
        for (i = 0; i < cmdnum; ++i) {//转换
            strr[i] = (jstring) (*env)->GetObjectArrayElement(env, cmdline, i);
            argv[i] = (char *) (*env)->GetStringUTFChars(env, strr[i], 0);
        }
    }
    //新建线程 执行ffmpeg 命令
    ffmpeg_thread_run_cmd(cmdnum, argv);
    //注册ffmpeg命令执行完毕时的回调
    ffmpeg_thread_callback(ffmpeg_callback);
    free(strr);
    return 0;
}

