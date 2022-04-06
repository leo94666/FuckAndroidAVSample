package com.top.ffmpeg.gles;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

/**
 * @author leo
 * @version 1.0
 * @className SuperGLSurfaceView
 * @description TODO
 * @date 2022/4/6 12:08
 **/
public class SuperGLSurfaceView extends GLSurfaceView {

    private SuperGLRender mGLRender;
    public SuperGLSurfaceView(Context context) {
        super(context);
    }

    public SuperGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }



}
