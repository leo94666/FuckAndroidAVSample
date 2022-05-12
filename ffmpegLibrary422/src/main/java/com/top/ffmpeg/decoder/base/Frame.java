package com.top.ffmpeg.decoder.base;

import android.media.MediaCodec;

import java.nio.ByteBuffer;

/**
 * @author leo
 * @version 1.0
 * @className Frame
 * @description TODO
 * @date 2022/5/12 16:42
 **/
public class Frame {

    private ByteBuffer buffer;

    private  MediaCodec.BufferInfo bufferInfo;

    public void setBufferInfo(MediaCodec.BufferInfo buffer) {
        this.bufferInfo = buffer;
    }

    public ByteBuffer getBuffer() {
        return buffer;
    }

    public void setBuffer(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    public MediaCodec.BufferInfo getBufferInfo() {
        return bufferInfo;
    }
}
