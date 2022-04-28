package com.top.av.ui

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import com.top.arch.base.BaseActivity
import com.top.arch.logger.Logger
import com.top.av.R
import com.top.av.databinding.ActivityFfmpegInfoBinding
import com.top.ffmpeg.FFmpegKits
import com.top.ffmpeg.ffmpeg.*
import kotlinx.android.synthetic.main.activity_ffmpeg_info.*

public class FFmpegInfoActivity : BaseActivity<ActivityFfmpegInfoBinding>() {


    override fun getLayout(): Int {
        setFullScreen()
        return R.layout.activity_ffmpeg_info
    }

    override fun init(root: View?) {
        FFmpegKitConfig.enableLogCallback {
            //Logger.i(it.toString())
            Log.i("ffmpegLibrary422--Java",it.message)
        }
        FFmpegKitConfig.enableStatisticsCallback {
            Log.i("ffmpegLibrary422--Java",it.toString())
        }

        mDataBinding.tvFfmpegVersion.text =
            String.format(
                resources.getString(
                    R.string.ffmpeg_version,
                    FFmpegKitConfig.getFFmpegVersion()
                )
            )
    }

    fun Run(view: android.view.View) {
        mDataBinding.tvResult.text=""
        val execute = FFmpegKit.execute(mDataBinding.etCmd.text.toString())
        mDataBinding.tvResult.text = execute.toString()

//        val execute = FFprobeKit.execute("/sdcard/1.mp4")
//        mDataBinding.tvResult.text = execute.toString()

//        val mediaInformation = FFprobeKit.getMediaInformation("/sdcard/1.mp4")
//        mDataBinding.tvResult.text = mediaInformation.toString()
    }

    fun urlProtocol(view: android.view.View) {
        tv_result.text = FFmpegKits.urlProtocolInfo()
    }

    fun avFormat(view: android.view.View) {
        tv_result.text = FFmpegKits.avFormatInfo()
    }

    fun avCodec(view: android.view.View) {
        tv_result.text = FFmpegKits.avCodecInfo()
    }

    fun avFilter(view: android.view.View) {
        tv_result.text = FFmpegKits.avFilterInfo()
    }

    companion object {
        fun start(context: Context) {
            var intent = Intent(context, FFmpegInfoActivity::class.java)
            context.startActivity(intent)
        }
    }


}
