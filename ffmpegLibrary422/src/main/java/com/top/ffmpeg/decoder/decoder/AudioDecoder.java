package com.top.ffmpeg.decoder.decoder;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaCodec;
import android.media.MediaFormat;

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
            channel = AudioFormat.CHANNEL_IN_MONO;
        } else {
            channel = AudioFormat.CHANNEL_IN_STEREO;
        }

        int minBufferSize = AudioTrack.getMinBufferSize(mSampleRate, channel, mPcmEncodeBit);
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
