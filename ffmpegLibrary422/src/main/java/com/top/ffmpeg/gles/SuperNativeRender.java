package com.top.ffmpeg.gles;

/**
 * @author leo
 * @version 1.0
 * @className SuperNativeRender
 * @description TODO
 * @date 2022/4/6 12:16
 **/
public class SuperNativeRender {

    static {
        System.loadLibrary("native-render");
    }

    public native void native_OnInit();

    public native void native_OnUnInit();

    public native void native_SetImageData(int format, int width, int height, byte[] bytes);

    public native void native_OnSurfaceCreated();

    public native void native_OnSurfaceChanged(int width, int height);

    public native void native_OnDrawFrame();
}
