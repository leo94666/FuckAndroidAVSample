package com.top.ffmpeg.ffmpeg;

import com.top.ffmpeg.ffmpeg.impl.Session;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @author leo
 * @version 1.0
 * @className FFmpegSession
 * @description TODO
 * @date 2022/4/8 15:00
 **/
public class FFmpegSession extends AbstractSession implements Session {

    private  List<Statistics> statistics;


    public FFmpegSession(String[] arguments) {
        super(arguments);
    }
}
