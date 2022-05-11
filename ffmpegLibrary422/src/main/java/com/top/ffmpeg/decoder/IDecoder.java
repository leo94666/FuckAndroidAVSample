package com.top.ffmpeg.decoder;

import android.media.MediaFormat;

public interface IDecoder extends Runnable {
    //暂停解码
    void pause();
    //继续解码
    void resume();
    //停止解码
    void stop();
    //是否正在解码
    boolean isDecoding();
    //是否正在快进
    boolean isSeeking();
    //是否停止解码
    boolean isStop();


    //获取视频宽
    int getWidth();
    //获取视频高
    int getHeight();
    //获取视频长
    long getDuration();
    //获取视频旋转角度
    int getRotationAngle();
    // 获取音视频对应的格式参数
    MediaFormat getMediaFormat();
    //获取音视频对应的媒体轨道
    int getTrack();

}
