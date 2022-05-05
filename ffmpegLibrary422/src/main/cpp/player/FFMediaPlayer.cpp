//
// Created by liyang on 2022/5/5.
//

#include "FFMediaPlayer.h"

FFMediaPlayer::~FFMediaPlayer() {

}

void FFMediaPlayer::Init(JNIEnv *env, jobject obj, char *url, int playerType, int renderType,
                         jobject surface) {

}

void FFMediaPlayer::UnInit() {

}

void FFMediaPlayer::Play() {

}

void FFMediaPlayer::Pause() {

}

void FFMediaPlayer::Stop() {

}

void FFMediaPlayer::SeekToPosition(float position) {

}

long FFMediaPlayer::GetMediaParams(int paramType) {
    return 0;
}

void FFMediaPlayer::SetMediaParams(int paramType, jobject obj) {

}

JNIEnv *FFMediaPlayer::GetJNIEnv(bool *isAttach) {
    return nullptr;
}

jobject FFMediaPlayer::GetJavaObj() {
    return nullptr;
}

JavaVM *FFMediaPlayer::GetJavaVM() {
    return nullptr;
}
