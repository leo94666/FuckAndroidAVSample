package com.top.av

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.top.ffmpeg.FFmpegCmd
import com.top.ffmpeg.info.VideoInfo
import com.top.ffmpeg.video.VideoEditor
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun version(view: android.view.View) {
        //VideoEditor.version()
        val value = cacheDir.absoluteFile.toString() + File.separator +System.currentTimeMillis()+ ".mp4"

        VideoEditor.addTextWatermark("/sdcard/out.mp4",value,30000,"i love you",object:VideoEditor.OnEditListener{
            override fun onSuccess() {

            }

            override fun onFailure() {

            }

            override fun onProgress(progress: Float) {

            }
        })
    }

    fun urlProtocolInfo(view: android.view.View) {
        tv.text = FFmpegCmd.urlProtocolInfo()
    }

    fun avFilter(view: android.view.View) {
        tv.text = FFmpegCmd.avFilterInfo()
    }

    fun avCodec(view: android.view.View) {
        tv.text = FFmpegCmd.avCodecInfo()

    }
    fun avFormat(view: android.view.View) {
        tv.text = FFmpegCmd.avFormatInfo()

    }
}