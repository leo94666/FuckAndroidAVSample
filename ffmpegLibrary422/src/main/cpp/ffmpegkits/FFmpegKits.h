/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_top_ffmpeg_cmd */

#ifndef _Included_com_top_ffmpeg_cmd
#define _Included_com_top_ffmpeg_cmd
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_top_ffmpeg_cmd
 * Method:    version
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_top_ffmpeg_FFmpegKits_version
  (JNIEnv *, jclass);

/*
 * Class:     com_top_ffmpeg_cmd
 * Method:    exec
 * Signature: (I[Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_com_top_ffmpeg_FFmpegKits_exec (JNIEnv *, jclass, jint, jobjectArray);

#ifdef __cplusplus
}
#endif
#endif

void ffmpeg_progress(float progress);