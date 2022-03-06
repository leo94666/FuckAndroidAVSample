package com.top.ffmpeg.video;

import android.util.Log;

import com.top.ffmpeg.CmdList;
import com.top.ffmpeg.FFmpegCmd;

/**
 * @Author leo
 * @ClassName VideoEditor
 * @Date 2022/3/6 17:11
 * @Version 1.0
 * @Description TODO
 **/
public class VideoEditor {
    private static final String TAG = "FFmpeg_JAVA_VideoEditor";


    public static void version() {
        CmdList cmd = new CmdList();
        cmd.append("ffmpeg");
        //cmd.append("-version");
        execCmd(cmd, 200, new OnEditListener() {
            @Override
            public void onSuccess() {
                Log.i(TAG,"====version======onSuccess");
            }

            @Override
            public void onFailure() {
                Log.i(TAG,"====version======onFailure");

            }

            @Override
            public void onProgress(float progress) {
                Log.i(TAG,"====version======onProgress:"+progress);
            }
        });
    }


    public static void addTextWatermark(String videoPath,String outPath ,long duration, String textWatermark, OnEditListener listener) {
        StringBuilder param = new StringBuilder();
        CmdList cmd = new CmdList();
        cmd.append("ffmpeg");
        cmd.append("-i").append(videoPath);
        cmd.append("-vf");
        param.append("drawtext=");
        param.append("text=").append(textWatermark).append(":").append("fontfile=/system/fonts/DroidSansMono.ttf").append(":fontsize=24:fontcolor=red");
        cmd.append(param.toString());
        cmd.append(outPath);
        execCmd(cmd, duration, listener);
    }



    private static void execCmd(CmdList cmd, long duration, final OnEditListener listener) {
        String[] cmds = cmd.toArray(new String[cmd.size()]);
        String cmdLog = "";
        for (String ss : cmds) {
            cmdLog = cmdLog + " " + ss;
        }
        Log.i(TAG, "cmd:" + cmdLog);
        FFmpegCmd.exec(cmds, duration, new FFmpegCmd.OnCmdExecListener() {
            @Override
            public void onSuccess() {
                listener.onSuccess();
            }

            @Override
            public void onFailure() {
                listener.onFailure();
            }

            @Override
            public void onProgress(float progress) {
                listener.onProgress(progress);
            }
        });
    }


    public interface OnEditListener {
        void onSuccess();

        void onFailure();

        void onProgress(float progress);
    }
}
