package com.top.ffmpeg.decoder.decoder;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaCodec;
import android.media.MediaFormat;
import android.media.MediaRecorder;

import com.top.ffmpeg.decoder.base.BaseDecoder;
import com.top.ffmpeg.decoder.base.IExtractor;
import com.top.ffmpeg.decoder.extractor.AudioExtractor;

import java.nio.ByteBuffer;

/**
 * @author leo
 * @version 1.0
 * @className AudioDecoder
 * @description TODO
 * @date 2022/5/12 14:25
 *
 * AudioTrack基本使用
 * 1. 构造方法
 * AudioTrack(int streamType, int sampleRateInHz, int channelConfig, int audioFormat, int bufferSizeInBytes, int mode)
 * streamType:          音频流类型， AudioManager.STREAM_****
 * sampleRateInHz:      采样率 MediaRecoder 的采样率通常是8000Hz,  AAC的通常是44100Hz
 * channelConfig:       声道数 单声道CHANNEL_IN_MONO,双声道CHANNEL_IN_STEREO
 * audioFormat:         数据位宽  只支持AudioFormat.ENCODING_PCM_8BIT（8bit）和AudioFormat.ENCODING_PCM_16BIT（16bit）两种，后者支持所有Android手机
 * bufferSizeInBytes:   建议使用AudioTrack.getMinBufferSize()这个方法获取，参数如上
 * mode:                播放模式 MODE_STATIC，一次性将所有数据都写入播放缓冲区中，简单高效，一般用于铃声，系统提醒音，内存比较小的。
 *                      MODE_STREAM，需要按照一定的时间间隔，不断的写入音频数据，理论上它可以应用于任何音频播放的场景。
 *
 * 2. 基本方法
 *  play()
 *  stop()
 *  release()
 *
 **/
public class AudioDecoder extends BaseDecoder {

    //采样率
    private int mSampleRate = -1;
    //声音通道数量
    private int mChannels = 1;
    //PCM采样位数
    private int mPcmEncodeBit = AudioFormat.ENCODING_PCM_16BIT;
    //音频播放器
    private AudioTrack mAudioTrack;
    //音频数据缓存
    private short[] mAudioOutTempBuf;


    public AudioDecoder(String mFilePath) {
        super(mFilePath);
    }

    @Override
    protected boolean check() {
        return false;
    }

    @Override
    protected IExtractor initExtractor(String filePath) {
        return new AudioExtractor(filePath);
    }

    @Override
    protected void initSpecParams(MediaFormat format) {
        try {
            mChannels = format.getInteger(MediaFormat.KEY_CHANNEL_COUNT);
            mSampleRate = format.getInteger(MediaFormat.KEY_SAMPLE_RATE);

            if (format.containsKey(MediaFormat.KEY_PCM_ENCODING)) {
                mPcmEncodeBit = format.getInteger(MediaFormat.KEY_PCM_ENCODING);
            } else {
                //如果没有这个参数，默认为16位采样
                mPcmEncodeBit = AudioFormat.ENCODING_PCM_16BIT;
            }
        } catch (Exception e) {

        }
    }

    @Override
    protected boolean configCodec(MediaCodec codec, MediaFormat format) {
        codec.configure(format, null, null, 0);
        return true;
    }

    @Override
    protected boolean initRender() {
        int channel;
        if (mChannels == 1) {
            channel = AudioFormat.CHANNEL_OUT_MONO;
        } else {
            channel = AudioFormat.CHANNEL_OUT_STEREO;
        }
        int minBufferSize = AudioTrack.getMinBufferSize(mSampleRate, channel, mPcmEncodeBit);
        mAudioOutTempBuf = new short[minBufferSize / 2];
        try {
            mAudioTrack = new AudioTrack(
                    AudioManager.STREAM_MUSIC,
                    mSampleRate,
                    channel,
                    mPcmEncodeBit,
                    minBufferSize,
                    AudioTrack.MODE_STREAM
            );
            mAudioTrack.play();

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    protected void render(ByteBuffer outputBuffer, MediaCodec.BufferInfo bufferInfo) {
        if (mAudioOutTempBuf.length < bufferInfo.size / 2) {
            mAudioOutTempBuf = new short[bufferInfo.size / 2];
        }
        outputBuffer.position(0);
        outputBuffer.asShortBuffer().get(mAudioOutTempBuf, 0, bufferInfo.size / 2);
        mAudioTrack.write(mAudioOutTempBuf, 0, bufferInfo.size / 2);
    }

    @Override
    protected void doneDecode() {
        mAudioTrack.stop();
        mAudioTrack.release();
    }
}
