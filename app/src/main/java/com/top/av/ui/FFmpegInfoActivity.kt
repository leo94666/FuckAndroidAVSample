package com.top.av.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.top.arch.base.BaseActivity
import com.top.av.R
import com.top.av.databinding.ActivityFfmpegInfoBinding

public class FFmpegInfoActivity : BaseActivity<ActivityFfmpegInfoBinding>() {


    override fun getLayout(): Int {
        return R.layout.activity_ffmpeg_info
    }

    override fun init(root: View?) {

    }

    companion object {
        fun start(context: Context) {
            var intent = Intent()
            context.startActivity(intent)
        }
    }


}
