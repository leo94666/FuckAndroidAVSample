package com.top.ffmpeg.gles;

import android.opengl.GLSurfaceView;

import com.top.arch.logger.Logger;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author leo
 * @version 1.0
 * @className SuperGLRender
 * @description TODO
 * @date 2022/4/6 12:10
 **/
public class SuperGLRender implements GLSurfaceView.Renderer {

    private SuperNativeRender superNativeRender;
    public SuperGLRender(SuperNativeRender superNativeRender) {
        this.superNativeRender = superNativeRender;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        Logger.d("onSurfaceCreated called with: gl=[" + gl10 + "],config = [" + eglConfig + "]");
        superNativeRender.native_OnSurfaceCreated();
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        Logger.d("onSurfaceCreated called with: gl=[" + gl10 + "],width = [" + width + "],height=" + "[" + height + "]");
        superNativeRender.native_OnSurfaceChanged(width,height);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        Logger.d("onSurfaceCreated called with: gl=[" + gl10 + "]");
        superNativeRender.native_OnDrawFrame();
    }
}
