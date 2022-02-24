package com.top.ffmpeg;

/**
 * @author leo
 * @version 1.0
 * @className FFmpeg
 * @description FFmpeg Java 入口
 * @date 2022/2/15 17:34
 **/
public class Cmd {

    static {
        System.loadLibrary("fftools");
        System.loadLibrary("cmd");
    }


    public static native String version();

    public  native int exec(int argc, String[] argv);
//
//    public  native void exit();


}
