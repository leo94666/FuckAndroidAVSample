package com.top.av.ui

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil.setContentView
import com.top.arch.base.BaseActivity
import com.top.av.R
import com.top.av.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<ActivityMainBinding>() {


    override fun init(root: View?) {
        initLisener()
    }

    override fun getLayout(): Int {
        return R.layout.activity_main
    }

    private fun initLisener() {
        btn_ffmpeg_info.setOnClickListener {
            FFmpegInfoActivity.start(MainActivity@ this)
        }
    }


}