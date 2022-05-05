//
// Created by liyang on 2022/5/5.
//

#ifndef AV_DECODER_H
#define AV_DECODER_H
extern "C" {
#include <libavcodec/avcodec.h>
#include <libavformat/avformat.h>
#include <libavutil/frame.h>
#include <libavutil/time.h>
#include <libavcodec/jni.h>
}

enum DecoderState {
    STATE_UNKNOWN,
    STATE_DECODING,
    STATE_PAUSE,
    STATE_STOP
}

class Decoder {
public:
    Decoder() {};

    ~Decoder() {};

    virtual void Start() = 0;

    virtual void Pause() = 0;

    virtual void Stop() = 0;

    virtual void GetDuration() = 0;

    virtual void SeekToPosition(float position) = 0;

    virtual float GetCurrentPosition() = 0;

private:
    int InitDecoder();

    void UnInitDecoder();

    //启动解码线程
    void StartDecodingThread();
    //

    AVFormatContext *m_AVFormatContext = nullptr;
};

#endif //AV_DECODER_H
