package com.top.ffmpeg.ffmpeg;

import com.top.ffmpeg.ffmpeg.impl.Session;

import java.util.Date;
import java.util.concurrent.Future;

/**
 * @author leo
 * @version 1.0
 * @className AbstractSession
 * @description TODO
 * @date 2022/4/8 16:01
 **/
public abstract class AbstractSession implements Session {

    /**
     * Session identifier.
     */
    protected long sessionId;


    /**
     * Date and time the session was created.
     */
    protected Date createTime;

    /**
     * Date and time the session was started.
     */
    protected Date startTime;

    /**
     * Date and time the session has ended.
     */
    protected Date endTime;

    /**
     * Command arguments as an array.
     */
    protected String[] arguments;


    /**
     * Log entry lock.
     */
    protected Object logsLock;

    /**
     * Future created for sessions executed asynchronously.
     */
    protected Future<?> future;

    /**
     * State of the session.
     */
    protected SessionState state;

    /**
     * Return code for the completed sessions.
     */
    protected ReturnCode returnCode;


    public AbstractSession(String[] arguments) {
        this.arguments = arguments;
        this.createTime = new Date();
        this.startTime = null;
        this.endTime = null;
        this.future = null;
        this.state = SessionState.CREATED;
        this.returnCode = null;
    }
    void startRunning() {
        this.state = SessionState.RUNNING;
        this.startTime = new Date();
    }

    void complete(final ReturnCode returnCode) {
        this.returnCode = returnCode;
        this.state = SessionState.COMPLETED;
        this.endTime = new Date();
    }

    void fail(final Exception exception) {
        //this.failStackTrace = Exceptions.getStackTraceString(exception);
        this.state = SessionState.FAILED;
        this.endTime = new Date();
    }


    @Override
    public long getSessionId() {
        return sessionId;
    }

    @Override
    public Date getCreateTime() {
        return createTime;
    }

    @Override
    public Date getStartTime() {
        return startTime;
    }

    @Override
    public Date getEndTime() {
        return endTime;
    }

    @Override
    public long getDuration() {
        Date startTime = this.startTime;
        Date endTime = this.endTime;
        if (startTime != null && endTime != null) {
            return (endTime.getTime() - startTime.getTime());
        }
        return 0;
    }

    @Override
    public String[] getArguments() {
        return arguments;
    }

    @Override
    public String getCommand() {
        return FFmpegNative.argumentsToString(arguments);
    }

    @Override
    public SessionState getState() {
        return state;
    }

    @Override
    public void cancel() {
        if (state == SessionState.RUNNING) {
            //FFmpegKits.cancel(sessionId);
        }
    }

    @Override
    public ReturnCode getReturnCode() {
        return returnCode;
    }
}
