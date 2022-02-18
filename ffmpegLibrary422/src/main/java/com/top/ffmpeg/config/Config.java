package com.top.ffmpeg.config;

/**
 * @author leo
 * @version 1.0
 * @className Config
 * @description ffmpeg 配置
 * @date 2022/2/15 11:44
 **/
public class Config {
    public static final String TAG = "Android-mobile-ffmpeg 422";

    static {
        System.loadLibrary("avutil");
        System.loadLibrary("swresample");
        System.loadLibrary("avcodec");
        System.loadLibrary("avformat");
        System.loadLibrary("swscale");
        System.loadLibrary("avfilter");
        System.loadLibrary("avdevice");
        System.loadLibrary("native-lib");
    }

}
