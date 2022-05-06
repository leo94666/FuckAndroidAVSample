//
// Created by liyang on 2022/5/5.
//

#ifndef AV_MEDIAPLAYER_H
#define AV_MEDIAPLAYER_H


#include <jni.h>

class MediaPlayer {
public:
    MediaPlayer() {};

    virtual ~MediaPlayer() {};

    virtual void Init(JNIEnv *env, jobject obj, char *url,int renderType, jobject surface) = 0;

    virtual void UnInit() = 0;

    virtual void Play() = 0;

    virtual void Pause() = 0;

    virtual void Stop() = 0;

    virtual void SeekToPosition(float position) = 0;

    virtual long GetMediaParams(int paramType) = 0;

    virtual void SetMediaParams(int paramType, jobject obj){};
    virtual JNIEnv *GetJNIEnv(bool *isAttach) = 0;

    virtual jobject GetJavaObj() = 0;

    virtual JavaVM *GetJavaVM() = 0;

    JavaVM *m_JavaVM = nullptr;
    jobject m_JavaObj = nullptr;

};


#endif //AV_MEDIAPLAYER_H
