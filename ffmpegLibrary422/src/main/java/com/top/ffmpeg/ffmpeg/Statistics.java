package com.top.ffmpeg.ffmpeg;

import java.io.Serializable;

/**
 * @author leo
 * @version 1.0
 * @className Statistics
 * @description TODO
 * @date 2022/4/8 14:56
 **/
public class Statistics implements Serializable {
    //会话id
    private long sessionId;
    //帧数量
    private int videoFrameNumber;

    private float videoFps;

    private float videoQuality;
    private long size;
    private int time;
    private double bitrate;
    private double speed;


    public long getSessionId() {
        return sessionId;
    }

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    public int getVideoFrameNumber() {
        return videoFrameNumber;
    }

    public void setVideoFrameNumber(int videoFrameNumber) {
        this.videoFrameNumber = videoFrameNumber;
    }

    public float getVideoFps() {
        return videoFps;
    }

    public void setVideoFps(float videoFps) {
        this.videoFps = videoFps;
    }

    public float getVideoQuality() {
        return videoQuality;
    }

    public void setVideoQuality(float videoQuality) {
        this.videoQuality = videoQuality;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public double getBitrate() {
        return bitrate;
    }

    public void setBitrate(double bitrate) {
        this.bitrate = bitrate;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }


    @Override
    public String toString() {
        return "Statistics[" +
                "sessionId=" + sessionId +
                ", videoFrameNumber=" + videoFrameNumber +
                ", videoFps=" + videoFps +
                ", videoQuality=" + videoQuality +
                ", size=" + size +
                ", time=" + time +
                ", bitrate=" + bitrate +
                ", speed=" + speed +
                ']';
    }
}
