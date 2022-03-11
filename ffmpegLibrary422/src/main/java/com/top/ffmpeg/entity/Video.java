package com.top.ffmpeg.entity;


import java.io.Serializable;

public class Video implements Serializable {
    private int id;
    private String videoName;
    private String coverPath; //封面路径
    private String videoPath;
    private long duration;
    private long videoSize;

    public Video(String videoPath) {
        this.videoPath = videoPath;
    }



    public Video(String videoName, String videoPath, long duration, long videoSize) {
        this.videoName = videoName;
        this.videoPath = videoPath;
        this.duration = duration;
        this.videoSize = videoSize;
    }

    public Video(int id, String videoName, String videoPath, long duration, long videoSize) {
        this.id = id;
        this.videoName = videoName;
        this.videoPath = videoPath;
        this.duration = duration;
        this.videoSize = videoSize;
    }

    public int getId() {
        return id;
    }

    public String getVideoName() {
        return videoName;
    }

    public long getDuration() {
        return duration;
    }

    public long getVideoSize() {
        return videoSize;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setVideoSize(long videoSize) {
        this.videoSize = videoSize;
    }

    public String getVideoPath() {
        return videoPath;
    }


    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }
}
