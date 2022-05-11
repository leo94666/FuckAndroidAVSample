package com.top.ffmpeg.decoder;

import android.media.MediaCodec;
import android.media.MediaFormat;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * @author leo
 * @version 1.0
 * @className Base
 * @description TODO
 * @date 2022/5/11 16:49
 **/
public abstract class BaseDecoder implements IDecoder {

    /////////////////////////////////////////线程相关////////////////////////

    private boolean mRunning = true;

    private Object mLock = new Object();

    private boolean mReadyForDecode = false;

    /////////////////////////////////////////解码相关////////////////////////

    //解码状态
    private DecodeState mState;

    //音视频解码器
    private MediaCodec mCodec;
    //音视频数据读取器
    private IExtractor mExtractor;
    //解码输入缓冲区

    private long mDuration = 0;


    private ByteBuffer[] mInputBuffers;
    private ByteBuffer[] mOutputBuffers;

    ////////////////////////////////////////////////////////////////////////
    private String mFilePath;

    public BaseDecoder(String mFilePath) {
        this.mFilePath = mFilePath;
    }

    @Override
    public void run() {
        mState = DecodeState.START;

        //1.初始化 2.启动解码器
        if (!init()) return;

        while (mRunning) {

        }
    }


    private boolean init() {
        if (mFilePath.isEmpty() || new File(mFilePath).exists()) {
            //文件不存在
            return false;
        }

        //初始化数据提取器
        mExtractor = initExtractor(mFilePath);
        if (mExtractor == null || mExtractor.getFormat() == null) return false;

        //初始化参数
        if (!initParam()) return false;
        //初始化渲染器
        if (!initRender()) return false;
        //初始化解码器
        if (!initCodec()) return false;
        return true;
    }


    private boolean initParam() {
        try {
            MediaFormat format = mExtractor.getFormat();
            mDuration = format.getLong(MediaFormat.KEY_DURATION) / 1000;
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private boolean initCodec() {
        try {
            String type = mExtractor.getFormat().getString(MediaFormat.KEY_MIME);
            mCodec = MediaCodec.createDecoderByType(type);

            mCodec.start();
            mInputBuffers = mCodec.getInputBuffers();
            mOutputBuffers = mCodec.getOutputBuffers();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    abstract IExtractor initExtractor(String filePath);

    abstract void render(ByteBuffer buffer, MediaCodec.BufferInfo bufferInfo);

    abstract void doneDecode();

    abstract boolean initRender();

}
