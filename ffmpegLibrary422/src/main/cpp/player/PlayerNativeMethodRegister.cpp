//
// Created by liyang on 2022/5/6.
//

#include "Player.h"

extern "C"
JNIEXPORT jlong JNICALL
Java_com_top_ffmpeg_player_FFMediaPlayer_native_1Init(JNIEnv *env, jobject thiz, jstring jurl,
                                                      jint playerType, jint renderType,
                                                      jobject surface) {
    const char *url = env->GetStringUTFChars(jurl, nullptr);
    Player *player = new Player();
    player->Init(env, thiz, const_cast<char *>(url), playerType, renderType, surface);
    env->ReleaseStringUTFChars(jurl, url);
    return reinterpret_cast<jlong>(player);
}
extern "C"
JNIEXPORT void JNICALL
Java_com_top_ffmpeg_player_FFMediaPlayer_native_1Play(JNIEnv *env, jobject thiz,
                                                      jlong player_handle) {
    if (player_handle != 0) {
        Player *pPlayerWrapper = reinterpret_cast<Player *>(player_handle);
        pPlayerWrapper->Play();
    }
}
extern "C"
JNIEXPORT void JNICALL
Java_com_top_ffmpeg_player_FFMediaPlayer_native_1SeekToPosition(JNIEnv *env, jobject thiz,
                                                                jlong player_handle,
                                                                jfloat position) {
    if (player_handle != 0) {
        Player *ffMediaPlayer = reinterpret_cast<Player *>(player_handle);
        ffMediaPlayer->SeekToPosition(position);
    }
}
extern "C"
JNIEXPORT void JNICALL
Java_com_top_ffmpeg_player_FFMediaPlayer_native_1Pause(JNIEnv *env, jobject thiz,
                                                       jlong player_handle) {
    if(player_handle != 0)
    {
        Player *ffMediaPlayer = reinterpret_cast<Player *>(player_handle);
        ffMediaPlayer->Pause();
    }

}
extern "C"
JNIEXPORT void JNICALL
Java_com_top_ffmpeg_player_FFMediaPlayer_native_1Stop(JNIEnv *env, jobject thiz,
                                                      jlong player_handle) {
    if(player_handle != 0)
    {
        Player *ffMediaPlayer = reinterpret_cast<Player *>(player_handle);
        ffMediaPlayer->Stop();
    }}
extern "C"
JNIEXPORT void JNICALL
Java_com_top_ffmpeg_player_FFMediaPlayer_native_1UnInit(JNIEnv *env, jobject thiz,
                                                        jlong player_handle) {
    if(player_handle != 0)
    {
        Player *ffMediaPlayer = reinterpret_cast<Player *>(player_handle);
        ffMediaPlayer->UnInit();
        delete ffMediaPlayer;
    }}
extern "C"
JNIEXPORT jlong JNICALL
Java_com_top_ffmpeg_player_FFMediaPlayer_native_1GetMediaParams(JNIEnv *env, jobject thiz,
                                                                jlong player_handle,
                                                                jint param_type) {
    long value = 0;
    if(player_handle != 0)
    {
        Player *ffMediaPlayer = reinterpret_cast<Player *>(player_handle);
        value = ffMediaPlayer->GetMediaParams(param_type);
    }
    return value;}
extern "C"
JNIEXPORT void JNICALL
Java_com_top_ffmpeg_player_FFMediaPlayer_native_1SetMediaParams(JNIEnv *env, jobject thiz,
                                                                jlong player_handle,
                                                                jint param_type, jobject param) {
    if(player_handle != 0)
    {
        Player *ffMediaPlayer = reinterpret_cast<Player *>(player_handle);
        ffMediaPlayer->SetMediaParams(param_type, param);
    }
}
extern "C"
JNIEXPORT void JNICALL
Java_com_top_ffmpeg_player_FFMediaPlayer_native_1OnSurfaceCreated(JNIEnv *env, jclass clazz,
                                                                  jint render_type) {
//    switch (render_type)
//    {
//        case VIDEO_GL_RENDER:
//            VideoGLRender::GetInstance()->OnSurfaceCreated();
//            break;
//        case AUDIO_GL_RENDER:
//            AudioGLRender::GetInstance()->OnSurfaceCreated();
//            break;
//        case VR_3D_GL_RENDER:
//            VRGLRender::GetInstance()->OnSurfaceCreated();
//            break;
//        default:
//            break;
//    }
}
extern "C"
JNIEXPORT void JNICALL
Java_com_top_ffmpeg_player_FFMediaPlayer_native_1OnSurfaceChanged(JNIEnv *env, jclass clazz,
                                                                  jint render_type, jint width,
                                                                  jint height) {

}
extern "C"
JNIEXPORT void JNICALL
Java_com_top_ffmpeg_player_FFMediaPlayer_native_1OnDrawFrame(JNIEnv *env, jclass clazz,
                                                             jint render_type) {

}
extern "C"
JNIEXPORT void JNICALL
Java_com_top_ffmpeg_player_FFMediaPlayer_native_1SetGesture(JNIEnv *env, jclass clazz,
                                                            jint render_type, jfloat x_rotate_angle,
                                                            jfloat y_rotate_angle, jfloat scale) {
    // TODO: implement native_SetGesture()
}
extern "C"
JNIEXPORT void JNICALL
Java_com_top_ffmpeg_player_FFMediaPlayer_native_1SetTouchLoc(JNIEnv *env, jclass clazz,
                                                             jint render_type, jfloat touch_x,
                                                             jfloat touch_y) {
    // TODO: implement native_SetTouchLoc()
}