package com.top.ffmpeg.video;

import android.os.Environment;
import android.util.Log;

import com.top.ffmpeg.CmdList;
import com.top.ffmpeg.FFmpegKits;
import com.top.ffmpeg.entity.Video;

import java.io.File;
import java.util.List;

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
        param.append("text=").append(textWatermark).append(":").append("fontsize=56:x=(w-text_w)/2:y=(h-text_h)/2:").append("fontfile=/system/fonts/DroidSansMono.ttf").append(":fontsize=24:fontcolor=red");
        cmd.append(param.toString());
        cmd.append(outPath);
        execCmd(cmd, duration, listener);
    }



    public static void cropVideo(String videoPath, long startTime, long endTime, OnEditListener listener) {
        cropVideo(videoPath, getSavePath(), startTime, endTime, listener);
    }

    public static void cropAndAddWaterMark(String videoPath, String dstPath, long startTime, long endTime, OnEditListener listener){
        long duration = endTime - startTime;
        CmdList cmd = new CmdList();
        cmd.append("ffmpeg");
        cmd.append("-y");
        cmd.append("-ss").append(startTime/ 1000).append("-t").append(duration / 1000).append("-accurate_seek");
        cmd.append("-i").append(videoPath);
        cmd.append("-codec").append("copy").append(dstPath);

        execCmd(cmd, duration, listener);
    }

    public static void cropVideo(String videoPath, String dstPath, long startTime, long endTime, OnEditListener listener) {
        long duration = endTime - startTime;
        CmdList cmd = new CmdList();
        cmd.append("ffmpeg");
        cmd.append("-y");
        cmd.append("-ss").append(startTime/ 1000).append("-t").append(duration / 1000).append("-accurate_seek");
        cmd.append("-i").append(videoPath);
        cmd.append("-codec").append("copy").append(dstPath);

        execCmd(cmd, duration, listener);
    }

    public static void mergeVideo(List<Video> videoList, OnEditListener listener) {
        long duration = 0;
        StringBuilder filterParams = new StringBuilder();
        CmdList cmd = new CmdList();
        cmd.append("ffmpeg");
        cmd.append("-y");

        for (int i = 0; i < videoList.size(); i++) {
            filterParams.append("[").append(i).append(":v]").append("scale=").append(720).append(":").append(1080)
                    .append(",setdar=").append("720/1080").append("[outv").append(i).append("];");
        }
        for (int i = 0; i < videoList.size(); i++) {
            Video video = videoList.get(i);
            filterParams.append("[outv").append(i).append("]");
            cmd.append("-i").append(video.getVideoPath());

            duration += video.getDuration();
        }
        filterParams.append("concat=n=").append(videoList.size()).append(":v=1:a=0[outv]");
        filterParams.append(";");
        for (int i = 0; i < videoList.size(); i++) {
            filterParams.append("[").append(i).append(":a]");
        }
        filterParams.append("concat=n=").append(videoList.size()).append(":v=0:a=1[outa]");
        cmd.append("-filter_complex");
        cmd.append(filterParams.toString());
        cmd.append("-map").append("[outv]");
        cmd.append("-map").append("[outa]");
        cmd.append(getSavePath());

        execCmd(cmd, duration, listener);
    }

    public static void mergeVideo2(String partListFile, long duration, OnEditListener listener) {
        CmdList cmd = new CmdList();
        cmd.append("ffmpeg");
        cmd.append("-f");
        cmd.append("concat");
        cmd.append("-i");
        cmd.append(partListFile);
        cmd.append("-c").append("copy");
        cmd.append(getSavePath());

        execCmd(cmd, duration, listener);
    }

    public static void addPictureWatermark(String videoPath, long duration, String watermarkPath,String outPath, OnEditListener listener) {
        StringBuilder param = new StringBuilder();
        CmdList cmd = new CmdList();
        cmd.append("ffmpeg");
        cmd.append("-i").append(videoPath);
        //cmd.append("scale=").append("1280:720");
        cmd.append("-threads").append(12);
        cmd.append("-preset").append("ultrafast");
        cmd.append("-r").append("12");
        //cmd.append("-an");
        // cmd.append("-filter_complex");
        cmd.append("-vf");
        //cmd.append("-vcodec").append("copy");
        //param.append("scale=720:480,");
        param.append("movie=").append(watermarkPath).append(",scale=").append(109).append(":").append(28).append("[watermark];");
        param.append("[in][watermark] ");
        //param.append("overlay=").append("main_w-overlay_w-").append(25).append(":").append(25).append("[out]");
        param.append("overlay=").append(10).append(":").append(10).append("[out]");
        cmd.append(param.toString());
        cmd.append("-vcodec").append("libx264");
        cmd.append(outPath);
        //commands[7] = "-y";//直接覆盖输出文件
        execCmd(cmd, duration, listener);
    }

    // ffmpeg -i 1.mp4 -i logo.png -i logo.png -filter_complex 'overlay=x=10:y=H-h-10,overlay=x=W-w-10:y=H-h-10' output.mp4
    public static void addPictureWatermark(String videoPath, long duration, String watermarkPath,String watermarkPath2,String outPath, OnEditListener listener) {
        StringBuilder param = new StringBuilder();
        CmdList cmd = new CmdList();
        cmd.append("ffmpeg");
        cmd.append("-hwaccel").append("cuvid");
        cmd.append("-i").append(videoPath);
        cmd.append("-i").append(watermarkPath);
        cmd.append("-i").append(watermarkPath2);
        //cmd.append("scale=").append("1280:720");
        cmd.append("-threads").append(16);
        cmd.append("-preset").append("ultrafast");
        cmd.append("-r").append("12");
        cmd.append("-filter_complex");
        param.append("overlay=").append("x=10").append(":").append("y=10").append(",").append("overlay=").append("x=W-w").append(":").append("y=H-h");
        cmd.append(param.toString());
        cmd.append("-vcodec").append("libx264");
        cmd.append(outPath);
        //commands[7] = "-y";//直接覆盖输出文件
        execCmd(cmd, duration, listener);
    }

    //ffmpeg -i 1.mp4 -threads 12 -preset ultrafast -r 12 -vf "[in]drawtext=x=w-text_w-10:y=h-line_h-10:text=HelloWorld:fontsize=24:fontcolor=yellow[text];movie=logo.png[wm];[text][wm]overlay=0:0,scale=1920:1080[out]" -vcodec libx264 22.mp4
    public static void addPictureAndTextWatermark(String videoPath, long duration, String watermarkPath,String outPath, OnEditListener listener) {
        StringBuilder param = new StringBuilder();
        CmdList cmd = new CmdList();
        cmd.append("ffmpeg");
        cmd.append("-i").append(videoPath);
        //cmd.append("scale=").append("1280:720");
        cmd.append("-threads").append(12);
        cmd.append("-preset").append("ultrafast");
        cmd.append("-r").append("12");
        //cmd.append("-an");
        // cmd.append("-filter_complex");
        cmd.append("-vf");
        //cmd.append("-vcodec").append("copy");
        //param.append("scale=720:480,");
        param.append("[in]drawtext=").append("x=").append("w-text_w-10").append(":").append("y=").append("h-line_h-10").append(":").append("text=").append("华西医院-李主任").append(":").append("fontsize=").append(24).append(":").append("fontcolor=").append("black").append("[text]").append(";");
        param.append("movie=").append(watermarkPath).append(",scale=").append(109).append(":").append(28).append("[watermark];");
        param.append("[in][watermark] ");
        //param.append("overlay=").append("main_w-overlay_w-").append(25).append(":").append(25).append("[out]");
        param.append("overlay=").append(10).append(":").append(10).append("[out]");
        cmd.append(param.toString());
        cmd.append("-vcodec").append("libx264");
        cmd.append(outPath);

        execCmd(cmd, duration, listener);
    }

    public static void addTextWatermark(String videoPath, long duration, String textWatermark, OnEditListener listener) {
        StringBuilder param = new StringBuilder();
        CmdList cmd = new CmdList();
        cmd.append("ffmpeg");
        cmd.append("-i").append(videoPath);
        cmd.append("-vf");
        param.append("drawtext=");
        param.append("text=").append(textWatermark).append(":").append("fontsize=").append(24).append(":").append("fontcolor=").append("white").append(":");
        param.append("x=").append(10).append(":").append("y=").append(10).append(":");
        param.append("shadowy=").append(2);
        cmd.append(param.toString());
        cmd.append(getSavePath());

        execCmd(cmd, duration, listener);
    }

    public static String getSavePath() {
        String savePath = Environment.getExternalStorageDirectory().getPath() + "/VideoEditor/";
        File file = new File(savePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return savePath + "out.mp4";
    }


    public static void version(OnEditListener listener) {
        CmdList cmd = new CmdList();
        cmd.append("ffmpeg");
        cmd.append("help");
        cmd.append("-filters");
        execCmd(cmd,1000, listener);
    }


    private static void execCmd(CmdList cmd, long duration, final OnEditListener listener) {
        String[] cmds = cmd.toArray(new String[cmd.size()]);
        String cmdLog = "";
        for (String ss : cmds) {
            cmdLog = cmdLog + " " + ss;
        }
        Log.i(TAG, "cmd:" + cmdLog);
        FFmpegKits.exec(cmds, duration, new FFmpegKits.OnCmdExecListener() {
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
