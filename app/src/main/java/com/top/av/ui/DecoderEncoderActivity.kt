package com.top.av.ui

import android.content.Context
import android.content.Intent
import android.os.Environment
import android.view.View
import com.top.arch.base.BaseActivity
import com.top.av.R
import com.top.av.databinding.ActivityDecoderEncoderBinding
import com.top.ffmpeg.decoder.decoder.AudioDecoder
import java.util.concurrent.Executors

class DecoderEncoderActivity: BaseActivity<ActivityDecoderEncoderBinding>() {
    companion object {
        fun start(context: Context) {
            var intent = Intent(context, DecoderEncoderActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun getLayout(): Int {
        return R.layout.activity_decoder_encoder
    }

    override fun init(root: View?) {

        val audioDecoder =
            AudioDecoder(Environment.getExternalStorageDirectory().absolutePath + "/1.mp4")


        val threadPool = Executors.newFixedThreadPool(10)
        threadPool.execute(audioDecoder)


        //audioDecoder.start()
    }
}
