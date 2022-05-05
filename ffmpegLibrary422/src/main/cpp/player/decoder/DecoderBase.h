//
// Created by liyang on 2022/5/5.
//

#ifndef AV_DECODERBASE_H
#define AV_DECODERBASE_H

extern "C" {
#include <libavcodec/avcodec.h>
#include <libavformat/avformat.h>
#include <libavutil/frame.h>
#include <libavutil/time.h>
#include <libavcodec/jni.h>
};
#include "TimeUtils.h"
#include "Decoder.h"
#include <thread>

#define MAX_PATH   2048
#define DELAY_THRESHOLD 100 //100ms

using namespace std;

enum DecoderState {
    STATE_UNKNOWN,
    STATE_DECODING,
    STATE_PAUSE,
    STATE_STOP
};

class DecoderBase : public Decoder {

public:
    void Start() override;

    void Pause() override;

    void Stop() override;

    float GetDuration() override;

    void SeekToPosition(float position) override;

    float GetCurrentPosition() override;

    virtual void ClearCache(){};

protected:
    virtual int Init(const char *url, AVMediaType mediaType);
    virtual void UnInit();
    virtual void OnDecoderReady() = 0;
    virtual void OnDecoderDone() = 0;
    //解码数据的回调
    virtual void OnFrameAvailable(AVFrame *frame) = 0;

    AVCodecContext *GetCodecContext() {
        return m_AVCodecContext;
    }
private:
    int InitFFDecoder();
    void UnInitDecoder();
    //启动解码线程
    void StartDecodingThread();
    //音视频解码循环
    void DecodingLoop();
    //更新显示时间戳
    void UpdateTimeStamp();
    //音视频同步
    long AVSync();
    //解码一个packet编码数据
    int DecodeOnePacket();
    //线程函数
    static void DoAVDecoding(DecoderBase *decoder);

    //封装格式上下文
    AVFormatContext *m_AVFormatContext = nullptr;
    //解码器上下文
    AVCodecContext *m_AVCodecContext = nullptr;
    //解码器
    AVCodec *m_AVCodec = nullptr;
    //编码的数据包
    AVPacket *m_Packet = nullptr;
    //解码的帧
    AVFrame *m_Frame = nullptr;
    //数据流的类型
    AVMediaType m_MediaType = AVMEDIA_TYPE_UNKNOWN;
    //文件地址
    char m_Url[MAX_PATH] = {0};
    //当前播放时间
    long m_CurTimeStamp = 0;
    //播放的起始时间
    long m_StartTimeStamp = -1;
    //总时长 ms
    long m_Duration = 0;
    //数据流索引
    int m_StreamIndex = -1;

    //seek position
    volatile float      m_SeekPosition = 0;
    volatile bool       m_SeekSuccess = false;
    //解码器状态
    volatile int  m_DecoderState = STATE_UNKNOWN;

    //锁和条件变量
    mutex               m_Mutex;
    condition_variable  m_Cond;
    thread             *m_Thread = nullptr;
};


#endif //AV_DECODERBASE_H
