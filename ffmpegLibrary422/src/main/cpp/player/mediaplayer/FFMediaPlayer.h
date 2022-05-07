//
// Created by liyang on 2022/5/5.
//

#ifndef AV_FFMEDIAPLAYER_H
#define AV_FFMEDIAPLAYER_H


#include <audio/AudioDecoder.h>
#include "MediaPlayer.h"
#include "video/VideoDecoder.h"
#include "../render/audio/OpenSLRender.h"

class FFMediaPlayer : public MediaPlayer {

public:
    FFMediaPlayer() {};

    virtual ~FFMediaPlayer();

    virtual void Init(JNIEnv *env, jobject obj, char *url, int renderType, jobject surface);

    virtual void UnInit();

    virtual void Play();

    virtual void Pause();

    virtual void Stop();

    virtual void SeekToPosition(float position);

    virtual long GetMediaParams(int paramType);

private:
    virtual JNIEnv *GetJNIEnv(bool *isAttach);

    virtual   jobject GetJavaObj();

    virtual JavaVM *GetJavaVM();

    VideoDecoder *m_VideoDecoder = nullptr;
    AudioDecoder *m_AudioDecoder = nullptr;

    VideoRender *m_VideoRender = nullptr;
    AudioRender *m_AudioRender = nullptr;
};


#endif //AV_FFMEDIAPLAYER_H
