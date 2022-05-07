//
// Created by liyang on 2022/5/7.
//

#ifndef AV_OPENSLRENDER_H
#define AV_OPENSLRENDER_H


#include "AudioRender.h"
#include <SLES/OpenSLES.h>
#include <SLES/OpenSLES_Android.h>
#include <queue>
#include <thread>
#define MAX_QUEUE_BUFFER_SIZE 3

class OpenSLRender : public AudioRender {
public:
    OpenSLRender() {}

    virtual ~OpenSLRender() {}

    virtual void Init();

    virtual void ClearAudioCache();

    virtual void RenderAudioFrame(uint8_t *pData, int dataSize);

    virtual void UnInit();

private:
    int CreateEngine();

    int CreateOutputMixer();

    int CreateAudioPlayer();

    int GetAudioFrameQueueSize();

    void StartRender();

    void HandleAudioFrameQueue();

    static void CreateSLWaitingThread(OpenSLRender *openSlRender);

    static void AudioPlayerCallback(SLAndroidSimpleBufferQueueItf bufferQueue, void *context);

    SLObjectItf m_EngineObj = nullptr;
    SLEngineItf m_EngineEngine = nullptr;
    SLObjectItf m_OutputMixObj = nullptr;
    SLObjectItf m_AudioPlayerObj = nullptr;
    SLPlayItf m_AudioPlayerPlay = nullptr;
    SLVolumeItf m_AudioPlayerVolume = nullptr;
    SLAndroidSimpleBufferQueueItf m_BufferQueue;

    std::queue<AudioFrame *> m_AudioFrameQueue;

    std::thread *m_thread = nullptr;
    std::mutex m_Mutex;
    std::condition_variable m_Cond;
    volatile bool m_Exit = false;
};


#endif //AV_OPENSLRENDER_H
