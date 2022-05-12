package com.top.ffmpeg.decoder.extractor;

import android.media.MediaExtractor;
import android.media.MediaFormat;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * @author leo
 * @version 1.0
 * @className MMExtractor
 * @description TODO
 * @date 2022/5/12 14:42
 **/
public class MMExtractor {

    private MediaExtractor mExtractor;

    //音频索引
    private int mAudioTrack = -1;
    //视频索引
    private int mVideoTrack = -1;
    //当前帧时间戳
    private long mCurSampleTime = 0;
    //当前帧标志
    private int mCurSampleFlag = 0;
    //开始解码时间点
    private long mStartPos = 0;

    private String mFilePath;

    public MMExtractor(String filePath) {
        mFilePath = filePath;
        mExtractor = new MediaExtractor();
        try {
            mExtractor.setDataSource(mFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public MediaFormat getVideoFormat() {
        for (int i = 0; i < mExtractor.getTrackCount(); i++) {
            MediaFormat trackFormat = mExtractor.getTrackFormat(i);
            String mime = trackFormat.getString(MediaFormat.KEY_MIME);
            if (mime.startsWith("video/")) {
                mVideoTrack = i;
                break;
            }
        }
        if (mVideoTrack >= 0) return mExtractor.getTrackFormat(mVideoTrack);
        else return null;
    }

    public MediaFormat getAudioFormat() {
        for (int i = 0; i < mExtractor.getTrackCount(); i++) {
            MediaFormat trackFormat = mExtractor.getTrackFormat(i);
            String mime = trackFormat.getString(MediaFormat.KEY_MIME);
            if (mime.startsWith("audio/")) {
                mAudioTrack = i;
                break;
            }
        }
        if (mAudioTrack >= 0) return mExtractor.getTrackFormat(mAudioTrack);
        else return null;
    }

    public int readBuffer(ByteBuffer byteBuffer){
        byteBuffer.clear();
        selectSourceTrack();
        int readSampleCount = mExtractor.readSampleData(byteBuffer, 0);
        if (readSampleCount<0){
            return -1;
        }
        mCurSampleTime=mExtractor.getSampleTime();
        mCurSampleFlag=mExtractor.getSampleFlags();
        mExtractor.advance();
        return readSampleCount;
    }

    private void selectSourceTrack(){
        if (mVideoTrack>=0){
            mExtractor.selectTrack(mVideoTrack);
        }else{
            mExtractor.selectTrack(mAudioTrack);
        }
    }


    public long seek(long pos){
        mExtractor.seekTo(pos,MediaExtractor.SEEK_TO_PREVIOUS_SYNC);
        return mExtractor.getSampleTime();
    }

    public void stop(){
        mExtractor.release();
        mExtractor=null;
    }
    public int getVideoTrack(){
        return mVideoTrack;
    }
    public int getAudioTrack(){
        return mAudioTrack;
    }

    public void setStartPos(long pos){
        mStartPos=pos;
    }
    public long getCurrentTimestamp(){
        return mCurSampleTime;
    }

    public long getSampleFlag(){
        return mCurSampleFlag;
    }
}
