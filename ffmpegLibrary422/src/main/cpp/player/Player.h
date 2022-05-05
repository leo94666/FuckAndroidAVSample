//
// Created by liyang on 2022/5/5.
//

#ifndef AV_PLAYER_H
#define AV_PLAYER_H


#include <jni.h>
#include "MediaPlayer.h"
#include "libavcodec/jni.h"

class Player {
public:
    Player() {};

    virtual ~Player() {};

    void Init(JNIEnv *env, jobject obj, char *url, int playerType, int renderType, jobject surface);

    void UnInit();

    void Play();

    void Pause();

    void Stop();

    void SeekToPosition(float position);

    long GetMediaParams(int paramType);

    void SetMediaParams(int paramType, jobject obj);

private:
    MediaPlayer *m_MediaPlayer = nullptr;
};


#endif //AV_PLAYER_H
