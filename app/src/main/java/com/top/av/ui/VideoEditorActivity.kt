package com.top.av.ui

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.MediaController
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.top.arch.base.BaseActivity
import com.top.arch.logger.Logger
import com.top.arch.model.MediaType
import com.top.arch.utils.StringUtils
import com.top.av.R
import com.top.av.adapter.FFmpegRecyclerViewAdapter
import com.top.av.adapter.OpenGLRecyclerViewAdapter
import com.top.av.bean.FFmpegCommand
import com.top.av.databinding.ActivityVideoEditorBinding
import com.top.ffmpeg.ffmpeg.*
import com.top.media.picker.MediaPicker
import com.top.media.picker.entity.MediaEntity
import com.top.media.picker.entity.MediaPickConstants

public class VideoEditorActivity : BaseActivity<ActivityVideoEditorBinding>(),
    FFmpegSessionCompleteCallback, StatisticsCallback, LogCallback {

    val SAMPLE_TITLES = mutableListOf(
        FFmpegCommand("剪裁", ""),
        FFmpegCommand("图片水印", ""),
        FFmpegCommand("文字水印", "-i %d$1 -vf \"drawtext=fontfile=/sdcard/simsun.ttc:text='复仇者联盟：终局之战':fontsize=24:fontcolor=red:x=20:y=20:shadowy=2:\" -vcodec libx264 /sdcard/22.mp4 -y"),
        FFmpegCommand("图片+文字水印", ""),
        FFmpegCommand("转GIF", ""),
        FFmpegCommand("音频提取", ""),
        FFmpegCommand("拼接", ""),
        FFmpegCommand("质量压缩", ""),
        FFmpegCommand("加减速", ""),
        FFmpegCommand("涂鸦", ""),
        FFmpegCommand("倒放", ""),
        FFmpegCommand("素描", ""),
        FFmpegCommand("色彩平衡", ""),
        FFmpegCommand("模糊", ""),
        FFmpegCommand("九宫格", ""),
        FFmpegCommand("添加贴纸", ""),
        FFmpegCommand("滤镜", ""),
        FFmpegCommand("分屏", "")
    )
    private var mSampleSelectedIndex: Int = 0

    private var mVideoPath: String? = null

    companion object {
        fun start(context: Context) {
            var intent = Intent(context, VideoEditorActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun hideTitleBar() {
        //super.hideTitleBar()
    }

    override fun getLayout(): Int {
        return R.layout.activity_video_editor
    }

    override fun init(root: View?) {
        mDataBinding.tvSelectVideo.setOnClickListener {
            MediaPicker.create(this)
                .setMediaType(MediaPickConstants.MEDIA_TYPE_VIDEO)
                .forResult(1000)
        }
        FFmpegKitConfig.enableLogCallback {
            //Logger.i(it.toString())
            android.util.Log.i("ffmpegLibrary422--Java",it.message)
        }
        FFmpegKitConfig.enableStatisticsCallback {
            android.util.Log.i("ffmpegLibrary422--Java",it.toString())
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 1000) {
            if (data != null) {
                val list: List<MediaEntity> = MediaPicker.obtainMediaResults(data)
                val one = list[0]
                mVideoPath = one.path
                initVideoView(one.path)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_ffmpeg_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_change_sample) {
            if (StringUtils.isTrimEmpty(mVideoPath)) {
                Toast.makeText(this, "请先选择视频", Toast.LENGTH_SHORT).show()
            } else {
                showFFmpegDialog()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initVideoView(videoPath: String) {
        mDataBinding.videoView.setVideoPath(videoPath)
        mDataBinding.videoView.setMediaController(MediaController(this))
        mDataBinding.videoView.start()
    }

    private fun showFFmpegDialog() {

        val builder = AlertDialog.Builder(this)
        val inflater = LayoutInflater.from(this)
        val rootView: View = inflater.inflate(R.layout.dialog_sample_selected_layout, null)

        val dialog = builder.create()

        val confirmBtn = rootView.findViewById<Button>(R.id.confirm_btn)
        confirmBtn.setOnClickListener {
            dialog.cancel()
            dealVideo()
        }

        val resolutionsListView: RecyclerView = rootView.findViewById(R.id.resolution_list_view)

        val myPreviewSizeViewAdapter =
            FFmpegRecyclerViewAdapter(this, SAMPLE_TITLES)
        myPreviewSizeViewAdapter.setSelectIndex(mSampleSelectedIndex)
        myPreviewSizeViewAdapter.addOnItemClickListener { view, position ->
            myPreviewSizeViewAdapter.selectIndex = position
            mSampleSelectedIndex = position
        }
        val manager = LinearLayoutManager(this)
        manager.orientation = LinearLayoutManager.VERTICAL
        resolutionsListView.layoutManager = manager

        resolutionsListView.adapter = myPreviewSizeViewAdapter
        resolutionsListView.scrollToPosition(mSampleSelectedIndex)

        dialog.show()
        dialog.window!!.setContentView(rootView)
    }

    private fun dealVideo() {

        runOnUiThread {
            showLoading()
        }
        FFmpegKit.executeAsync(SAMPLE_TITLES[mSampleSelectedIndex].cmd,this,this,this)
    }

    override fun apply(session: FFmpegSession?) {
        Logger.i(session.toString())

        runOnUiThread { hideLoading() }

        //session?.future
        //Toast.makeText(this, "处理成功", Toast.LENGTH_SHORT).show()
    }

    override fun apply(statistics: Statistics?) {
        Logger.i(statistics.toString())
    }

    override fun apply(log: Log?) {
        Logger.i(log.toString())
    }

}