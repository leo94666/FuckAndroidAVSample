//
// Created by liyang on 2022/5/5.
//

#ifndef AV_FFMEDIAPLAYER_H
#define AV_FFMEDIAPLAYER_H


#include "MediaPlayer.h"

class FFMediaPlayer : public MediaPlayer {

public:
    FFMediaPlayer() {};

    ~FFMediaPlayer() override;

    void Init(JNIEnv *env, jobject obj, char *url, int playerType, int renderType,
              jobject surface) override;

    void UnInit() override;

    void Play() override;

    void Pause() override;

    void Stop() override;

    void SeekToPosition(float position) override;

    long GetMediaParams(int paramType) override;

    void SetMediaParams(int paramType, jobject obj) override;

private:
    JNIEnv *GetJNIEnv(bool *isAttach) override;

    jobject GetJavaObj() override;

    JavaVM *GetJavaVM() override;
};


#endif //AV_FFMEDIAPLAYER_H
