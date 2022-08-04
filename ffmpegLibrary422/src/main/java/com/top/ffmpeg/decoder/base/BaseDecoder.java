package com.top.ffmpeg.decoder.base;

import android.media.AudioTrack;
import android.media.MediaCodec;
import android.media.MediaCrypto;
import android.media.MediaDrm;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.media.MediaSync;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

/**
 * @author leo
 * @version 1.0
 * @className Base
 * @description TODO
 * @date 2022/5/11 16:49
 **/
public abstract class BaseDecoder implements IDecoder {


    private String TAG = "MediaCodec-BaseDecoder";
    /////////////////////////////////////////线程相关////////////////////////

    private boolean mRunning = true;

    //线程等待锁
    private final Object mLock = new Object();


    /////////////////////////////////////////解码相关////////////////////////

    //解码状态
    private DecodeState mState;

    //音视频解码器
    private MediaCodec mCodec;
    //private MediaExtractor mExtractor;
    //private MediaSync mMediaSync;
    //private MediaMuxer mMediaMuxer;
    //private MediaCrypto mMediaCrypto;
    //private MediaDrm mMediaDrm;
    //private AudioTrack mAudioTrack;

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
        mBufferInfo = new MediaCodec.BufferInfo();

    }

    @Override
    public void run() {
        mState = DecodeState.START;
        //1.初始化 2.启动解码器
        if (!init()) return;
        try {
            Log.i(TAG, "================Decode start");
            while (mRunning) {
                if (mState != DecodeState.START && mState != DecodeState.DECODING && mState != DecodeState.SEEKING) {
                    waitDecode();
                    Log.i(TAG, "================waitDecode");
                    mStartTimeForSync = System.currentTimeMillis() - getCurTimeStamp();
                }

                if (!mRunning || mState == DecodeState.STOP) {
                    mRunning = false;
                    break;
                }

                if (mStartTimeForSync == -1L) {
                    mStartTimeForSync = System.currentTimeMillis();
                }

                if (!mIsEOS) {
                    //如果数据没有解码完毕，将数据推入解码器解码
                    mIsEOS = pushBufferToDecoder();
                }
                //将解码好的数据从缓冲区取出来
                int index = pullBufferFromDecoder();
                if (index >= 0) {
                    //音视频同步
                    if (mSyncRender && mState == DecodeState.DECODING) {
                        sleepRender();
                    }
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
                        Log.i(TAG, "================Decode finish");
                    }
                }
            }

        } catch (Exception e) {
            Log.i(TAG, "==============");
        } finally {
            doneDecode();
            release();
        }
    }

    private void sleepRender() {
        long passTime = System.currentTimeMillis() - mStartTimeForSync;
        long curTime = getCurTimeStamp();
        if (curTime > passTime) {
            try {
                Thread.sleep(curTime - passTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void release() {
        try {
            mState = DecodeState.STOP;
            mIsEOS = false;
            mExtractor.stop();
            mCodec.stop();
            mCodec.release();
        }catch(Exception e){

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
            if (mEndPos == 0L) mEndPos = mDuration;
            initSpecParams(mExtractor.getFormat());
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private boolean initCodec() {
        try {
            String type = mExtractor.getFormat().getString(MediaFormat.KEY_MIME);
            mCodec = MediaCodec.createDecoderByType(type);
            if (!configCodec(mCodec, mExtractor.getFormat())) {
                waitDecode();
            }
            mCodec.start();
            //获取需要编码数据的输入流队列，返回的是一个ByteBuffer数组
            mInputBuffers = mCodec.getInputBuffers();
            //获取编解码之后的数据输出流队列，返回的是一个ByteBuffer数组
            mOutputBuffers = mCodec.getOutputBuffers();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean pushBufferToDecoder() {
        //从输入流队列中取数据进行编码操作
        int inputBufferIndex = mCodec.dequeueInputBuffer(1000);
        boolean isEndOfStream = false;
        if (inputBufferIndex >= 0) {
            ByteBuffer inputBuffer = mInputBuffers[inputBufferIndex];
            int sampleSize = mExtractor.readBuffer(inputBuffer);
            if (sampleSize < 0) {
                //输入流入队列
                mCodec.queueInputBuffer(inputBufferIndex, 0, 0, 0, MediaCodec.BUFFER_FLAG_END_OF_STREAM);
                isEndOfStream = true;
            } else {
                //输入流入队列
                mCodec.queueInputBuffer(inputBufferIndex, 0, sampleSize, mExtractor.getCurrentTimestamp(), 0);
            }
        }
        return isEndOfStream;
    }

    private int pullBufferFromDecoder() {
        //从输出队列中取出编码操作之后的数据
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

    protected void notifyDecode() {
        synchronized (mLock) {
            //唤醒对象的等待池中的所有线程，进入锁池。
            mLock.notifyAll();
            //随机唤醒对象的等待池中的一个线程，进入锁池
            //mLock.notify();
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
        notifyDecode();
    }

    @Override
    public void stop() {
        mState = DecodeState.STOP;
        mRunning = false;
        notifyDecode();
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
    public long getCurTimeStamp() {
        return mBufferInfo.presentationTimeUs / 1000;
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
