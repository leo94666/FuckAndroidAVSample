package com.top.av.ui

import android.content.Context
import android.content.Intent
import android.os.Environment
import android.view.View
import com.top.arch.base.BaseActivity
import com.top.arch.utils.DataCenter
import com.top.arch.utils.ThreadUtils
import com.top.av.R
import com.top.av.databinding.ActivityDecoderEncoderBinding
import com.top.ffmpeg.decoder.decoder.AudioDecoder
import com.top.ffmpeg.decoder.decoder.VideoDecoder
import java.util.concurrent.Executors

class DecoderEncoderActivity: BaseActivity<ActivityDecoderEncoderBinding>() {

    var audioDecoder: AudioDecoder? = null
    var videoDecoder: VideoDecoder? = null

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
        keepScreenOn()
        val s = Environment.getExternalStorageDirectory().absolutePath + "/1.mp4"
        audioDecoder = AudioDecoder(s)
        videoDecoder= VideoDecoder(s,mDataBinding.surfaceView,null)

        val threadPool = Executors.newFixedThreadPool(10)
        threadPool.execute(audioDecoder)
        //threadPool.execute(videoDecoder)

        audioDecoder?.resume()
       // videoDecoder?.resume()
    }

    override fun onDestroy() {
        super.onDestroy()
        audioDecoder?.stop()
        videoDecoder?.stop()

    }
}

