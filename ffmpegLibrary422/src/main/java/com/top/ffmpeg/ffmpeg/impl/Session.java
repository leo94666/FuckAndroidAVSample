package com.top.ffmpeg.ffmpeg.impl;

import com.top.ffmpeg.ffmpeg.ReturnCode;
import com.top.ffmpeg.ffmpeg.SessionState;

import java.util.Date;

public interface Session {
    long getSessionId();
    Date getCreateTime();
    Date getStartTime();
    Date getEndTime();
    long getDuration();
    String[] getArguments();
    String getCommand();
    SessionState getState();
    ReturnCode getReturnCode();
    void cancel();
}
