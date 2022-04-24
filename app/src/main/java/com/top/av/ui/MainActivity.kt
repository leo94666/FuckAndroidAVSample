package com.top.av.ui

import android.Manifest
import android.view.View
import android.widget.Toast
import com.top.arch.base.BaseActivity
import com.top.arch.permissions.SuperPermission
import com.top.arch.permissions.callback.OnRequestCallback
import com.top.av.R
import com.top.av.databinding.ActivityMainBinding
import com.top.ffmpeg.CmdList
import com.top.ffmpeg.FFmpegKits
import com.top.ffmpeg.ffmpeg.FFmpegKit
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

        btn_cpu_info.setOnClickListener {
            CpuFeatureActivity.start(this)
        }

        btn_ffmpeg_info.setOnClickListener {


            FFmpegInfoActivity.start(MainActivity@ this)
        }

        btn_opengl_es.setOnClickListener {
            OpenGLESActivity.start(MainActivity@ this)
        }

        btn_video_editor.setOnClickListener {

            SuperPermission.init(this).permissions(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ).request { allGranted, grantedList, deniedList ->
                if (allGranted){
//                   var cmd =FFmpegNative.parseArguments("ffmpeg  -i /sdcard/1.mp4 -vf  \"[in]drawtext=fontfile=/sdcard/MesloLGSNFRegular.ttf:text='%{localtime\\:%T}':x=0:y=0:fontsize=20:fontcolor=yellow:shadowy=2[text];movie=/sdcard/logo.png[wm];[text][wm]overlay=main_w-overlay_w-10:main_h-overlay_h-10[out]\"  -c:v libx264 -an -f mp4 /sdcard/output.mp4 -y")
//                   // cmd=FFmpegNative.parseArguments("ffprobe")
//
//                    FFmpegKits.exec(
//                        cmd,
//                        2000,
//                        object : FFmpegKits.OnCmdExecListener {
//                            override fun onSuccess() {
//                                //Toast.makeText(this@MainActivity, "onSuccess", Toast.LENGTH_SHORT).show()
//                                runOnUiThread {
//                                    Toast.makeText(this@MainActivity, "onSuccess", Toast.LENGTH_SHORT).show()
//                                }
//                            }
//
//                            override fun onFailure() {
//                             //   Toast.makeText(this@MainActivity, "onFailure", Toast.LENGTH_SHORT).show()
//                            }
//
//                            override fun onProgress(progress: Float) {
//
//                            }
//                        })
                }
            }


        }
    }


}