package com.top.av.ui

import android.view.View
import com.top.arch.base.BaseActivity
import com.top.av.R
import com.top.av.databinding.ActivityMainBinding
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
    }


}