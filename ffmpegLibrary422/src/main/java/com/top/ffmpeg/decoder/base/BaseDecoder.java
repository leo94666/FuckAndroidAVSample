package com.top.ffmpeg.decoder.base;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.util.Log;

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
    private ByteBuffer[] mInputBuffers;
    private ByteBuffer[] mOutputBuffers;

    private MediaCodec.BufferInfo mBufferInfo;

    /**
     * 流数据是否结束
     */
    private boolean mIsEOS = false;

    protected int mVideoWidth = 0;

    protected int mVideoHeight = 0;

    private long mDuration = 0;

    private long mStartPos = 0;

    private long mEndPos = 0;

    /**
     * 开始解码时间，用于音视频同步
     */
    private long mStartTimeForSync = -1L;

    // 是否需要音视频渲染同步
    private boolean mSyncRender = true;


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
            if (!mIsEOS) {
                //如果数据没有解码完毕，将数据推入解码器解码
                mIsEOS = pushBufferToDecoder();
            }

            int index = pullBufferFromDecoder();
            if (index >= 0) {
                if (mSyncRender) {
                    render(mOutputBuffers[index], mBufferInfo);
                }
                Frame frame = new Frame();
                frame.setBuffer(mOutputBuffers[index]);
                frame.setBufferInfo(mBufferInfo);

                mCodec.releaseOutputBuffer(index, true);

                //【解码步骤：6. 判断解码是否完成】
                if (mBufferInfo.flags == MediaCodec.BUFFER_FLAG_END_OF_STREAM) {
                    mState = DecodeState.FINISH;
                }
            }
        }
    }


    private boolean init() {
        if (mFilePath.isEmpty() || !new File(mFilePath).exists()) {
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

    private boolean pushBufferToDecoder() {
        int inputBufferIndex = mCodec.dequeueInputBuffer(2000);
        boolean isEndOfStream = false;
        if (inputBufferIndex >= 0) {
            ByteBuffer inputBuffer = mInputBuffers[inputBufferIndex];
            int sampleSize = mExtractor.readBuffer(inputBuffer);
            if (sampleSize < 0) {
                mCodec.queueInputBuffer(inputBufferIndex, 0, 0, 0, MediaCodec.BUFFER_FLAG_END_OF_STREAM);
                isEndOfStream = true;
            } else {
                mCodec.queueInputBuffer(inputBufferIndex, 0, sampleSize, mExtractor.getCurrentTimestamp(), 0);
            }
        }
        return isEndOfStream;
    }

    private int pullBufferFromDecoder() {
        int index = mCodec.dequeueOutputBuffer(mBufferInfo, 1000);
        switch (index) {
            case MediaCodec.INFO_OUTPUT_FORMAT_CHANGED:
            case MediaCodec.INFO_TRY_AGAIN_LATER: {
                break;
            }
            case MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED: {
                mOutputBuffers = mCodec.getOutputBuffers();
                break;
            }
            default: {
                return index;
            }
        }
        return -1;
    }

    private void waitDecode() {
        try {
            synchronized (mLock) {
                mLock.wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void notifyDecode() {
        synchronized (mLock) {
            mLock.notifyAll();
        }
    }

    //检查子类参数
    protected abstract boolean check();

    //初始化数据提取器
    protected abstract IExtractor initExtractor(String filePath);

    //初始化子类自己特有的参数
    protected abstract void initSpecParams(MediaFormat format);

    //配置解码器
    protected abstract boolean configCodec(MediaCodec codec, MediaFormat format);

    //初始化渲染器
    protected abstract boolean initRender();

    //渲染
    protected abstract void render(ByteBuffer buffer, MediaCodec.BufferInfo bufferInfo);

    //解码结束
    protected abstract void doneDecode();


    @Override
    public void pause() {
        mState = DecodeState.PAUSE;
    }

    @Override
    public void resume() {
        mState = DecodeState.DECODING;

    }

    @Override
    public void stop() {
        mState = DecodeState.STOP;
        mRunning = false;
    }

    @Override
    public boolean isDecoding() {
        return mState == DecodeState.DECODING;
    }

    @Override
    public boolean isSeeking() {
        return mState == DecodeState.SEEKING;
    }

    @Override
    public boolean isStop() {
        return mState == DecodeState.STOP;
    }

    @Override
    public int getWidth() {
        return mVideoWidth;
    }

    @Override
    public int getHeight() {
        return mVideoHeight;
    }

    @Override
    public long getDuration() {
        return mDuration;
    }

    @Override
    public int getRotationAngle() {
        return 0;
    }

    @Override
    public MediaFormat getMediaFormat() {
        return mExtractor.getFormat();
    }

    @Override
    public int getTrack() {
        return 0;
    }
}
