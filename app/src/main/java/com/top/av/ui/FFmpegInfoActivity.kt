package com.top.av.ui

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Toast
import com.top.arch.base.BaseActivity
import com.top.av.R
import com.top.av.databinding.ActivityFfmpegInfoBinding
import com.top.ffmpeg.FFmpegKits
import com.top.ffmpeg.ffmpeg.FFmpegNative
import kotlinx.android.synthetic.main.activity_ffmpeg_info.*

public class FFmpegInfoActivity : BaseActivity<ActivityFfmpegInfoBinding>() {


    override fun getLayout(): Int {
        setFullScreen()
        return R.layout.activity_ffmpeg_info
    }

    override fun init(root: View?) {
        mDataBinding.tvFfmpegVersion.text =
            String.format(resources.getString(R.string.ffmpeg_version,FFmpegNative.ffmpegVersion()))
    }

    fun Run(view: android.view.View) {
//        FFmpegCmd.exec( ,1000,object :FFmpegCmd.OnCmdExecListener{
//            override fun onSuccess() {
//
//            }
//            override fun onFailure() {
//
//            }
//            override fun onProgress(progress: Float) {
//
//            }
//        })
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
