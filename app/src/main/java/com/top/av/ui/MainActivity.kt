package com.top.av.ui

import android.view.View
import android.widget.Toast
import com.top.arch.base.BaseActivity
import com.top.av.R
import com.top.av.databinding.ActivityMainBinding
import com.top.ffmpeg.ffmpeg.FFmpegKit
import com.top.ffmpeg.ffmpeg.FFmpegNative
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<ActivityMainBinding>() {


    override fun init(root: View?) {
        initLisener()
    }

    override fun getLayout(): Int {
        setFullScreen()
        return R.layout.activity_main
    }

    private fun initLisener() {
        btn_ffmpeg_info.setOnClickListener {


            FFmpegInfoActivity.start(MainActivity@ this)
        }

        btn_opengl_es.setOnClickListener {
            OpenGLESActivity.start(MainActivity@ this)
        }
    }


}