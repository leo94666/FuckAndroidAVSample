package com.top.ffmpeg;


import kotlin.Deprecated;

/**
 * @author leo
 * @version 1.0
 * @className FFmpeg
 * @description FFmpeg Java 入口
 * @date 2022/2/15 17:34
 **/
@Deprecated(message = "废弃")
public class FFmpegKits {

    static {
        System.loadLibrary("ffmpeg");
        System.loadLibrary("FFmpegKits");
    }

    private static OnCmdExecListener sOnCmdExecListener;
    private static long sDuration;

    public static native String version();

    private static native int exec(int argc, String[] argv);

    public static native String urlProtocolInfo();

    public static native String avFormatInfo();

    public static native String avCodecInfo();

    public static native String avFilterInfo();


    public static void exec(String[] cmds, long duration, OnCmdExecListener listener) {
        sOnCmdExecListener = listener;
        sDuration = duration;
        exec(cmds.length, cmds);
    }

    public static void onExecuted(int ret) {
        if (sOnCmdExecListener != null) {
            if (ret == 0) {
                sOnCmdExecListener.onProgress(sDuration);
                sOnCmdExecListener.onSuccess();
            } else {
                sOnCmdExecListener.onFailure();
            }
        }
    }

    public static void onProgress(float progress) {
        if (sOnCmdExecListener != null) {
            if (sDuration != 0) {
                sOnCmdExecListener.onProgress(progress / (sDuration / 1000) * 0.95f);
            }
        }
    }

    public interface OnCmdExecListener {
        void onSuccess();

        void onFailure();

        void onProgress(float progress);
    }
}


