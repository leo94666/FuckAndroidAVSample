package com.top.ffmpeg.ffmpeg;

/**
 * @author leo
 * @version 1.0
 * @className FFmpegKit
 * @description TODO
 * @date 2022/4/8 16:11
 **/
public class FFmpegKit {

    public static FFmpegSession executeWithArguments(final String[] arguments) {
        final FFmpegSession session = new FFmpegSession(arguments);

        FFmpegNative.ffmpegExecute(session);

        return session;
    }


}
