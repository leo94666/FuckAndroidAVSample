package com.top.ffmpeg.decoder.extractor;

import android.media.MediaFormat;

import com.top.ffmpeg.decoder.base.IExtractor;

import java.nio.ByteBuffer;

/**
 * @author leo
 * @version 1.0
 * @className AudioExtractor
 * @description TODO
 * @date 2022/5/12 14:39
 **/
public class AudioExtractor implements IExtractor {

    private MMExtractor mMMExtractor;

    public AudioExtractor(String mFilePath) {
        mMMExtractor = new MMExtractor(mFilePath);
    }

    @Override
    public MediaFormat getFormat() {
        return mMMExtractor.getAudioFormat();
    }

    @Override
    public int readBuffer(ByteBuffer buffer) {
        return mMMExtractor.readBuffer(buffer);
    }

    @Override
    public long getCurrentTimestamp() {
        return mMMExtractor.getCurrentTimestamp();
    }

    @Override
    public long seek(long pos) {
        return mMMExtractor.seek(pos);
    }

    @Override
    public void setStartPos(long pos) {
        mMMExtractor.setStartPos(pos);
    }

    @Override
    public void stop() {
        mMMExtractor.stop();
    }
}
