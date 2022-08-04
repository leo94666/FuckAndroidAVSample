package com.top.ffmpeg.decoder.decoder;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.top.ffmpeg.decoder.base.BaseDecoder;
import com.top.ffmpeg.decoder.base.IExtractor;
import com.top.ffmpeg.decoder.extractor.VideoExtractor;

import java.nio.ByteBuffer;

/**
 * @author leo
 * @version 1.0
 * @className VideoDecoder
 * @description TODO
 * @date 2022/5/17 11:06
 **/
public class VideoDecoder extends BaseDecoder {

    private SurfaceView mSuperView;
    private Surface mSurface;

    public VideoDecoder(String mFilePath, SurfaceView superView, Surface surface) {
        super(mFilePath);
        this.mSuperView = superView;
        this.mSurface = surface;
    }

    @Override
    protected boolean check() {
        if (mSuperView == null && mSurface == null) {
            return false;
        }
        return false;
    }

    @Override
    protected IExtractor initExtractor(String filePath) {
        return new VideoExtractor(filePath);
    }

    @Override
    protected void initSpecParams(MediaFormat format) {
        int width = format.getInteger(MediaFormat.KEY_WIDTH);
        int height = format.getInteger(MediaFormat.KEY_HEIGHT);
    }

    @Override
    protected boolean configCodec(MediaCodec codec, MediaFormat format) {
        if (mSurface != null) {
            codec.configure(format, mSurface, null, 0);
            notifyDecode();
        } else if (mSuperView.getHolder().getSurface() != null) {
            mSurface = mSuperView.getHolder().getSurface();
            configCodec(codec, format);
        } else {
            mSuperView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(@NonNull SurfaceHolder holder) {
                    mSurface = holder.getSurface();
                    configCodec(codec, format);
                }

                @Override
                public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

                }

                @Override
                public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

                }
            });
            return false;
        }
        return true;
    }

    @Override
    protected boolean initRender() {
        return true;
    }

    @Override
    protected void render(ByteBuffer buffer, MediaCodec.BufferInfo bufferInfo) {

    }

    @Override
    protected void doneDecode() {

    }
}
