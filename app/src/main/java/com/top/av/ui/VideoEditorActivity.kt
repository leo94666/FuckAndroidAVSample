package com.top.av.ui

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import android.view.*
import android.widget.MediaController
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.top.androidx.dialog.OnDialogClickListener
import com.top.androidx.dialog.ShowDirection
import com.top.androidx.dialog.SuperDialog
import com.top.arch.base.BaseActivity
import com.top.arch.logger.Logger
import com.top.arch.utils.BitmapUtils
import com.top.arch.utils.FileUtils
import com.top.arch.utils.StringUtils
import com.top.av.R
import com.top.av.adapter.FFmpegRecyclerViewAdapter
import com.top.av.bean.FFmpegCommand
import com.top.av.databinding.ActivityVideoEditorBinding
import com.top.ffmpeg.ffmpeg.*
import com.top.media.picker.MediaPicker
import com.top.media.picker.entity.MediaEntity
import com.top.media.picker.entity.MediaPickConstants
import java.io.File
import java.lang.StringBuilder
import java.util.ArrayList

public class VideoEditorActivity : BaseActivity<ActivityVideoEditorBinding>(),
    FFmpegSessionCompleteCallback, StatisticsCallback, LogCallback {

    lateinit var SAMPLE_TITLES: ArrayList<FFmpegCommand>

    //    val SAMPLE_TITLES = mutableListOf(
//        FFmpegCommand("剪裁", ""),
//        FFmpegCommand("图片水印", ""),
//        FFmpegCommand(
//            "文字水印",
//            "-i /sdcard/1.mp4 -vf \"drawtext=fontfile=/sdcard/simsun.ttc:text='I Love You, 复仇者联盟：终局之战':fontsize=24:fontcolor=red:x=20:y=20:shadowy=2:\" -vcodec libx264 /data/data/com.top.av/cache/222.mp4 -y"
//        ),
//        FFmpegCommand("图片+文字水印", ""),
//        FFmpegCommand("转GIF", ""),
//        FFmpegCommand("音频提取", ""),
//        FFmpegCommand("拼接", ""),
//        FFmpegCommand("质量压缩", ""),
//        FFmpegCommand("加减速", ""),
//        FFmpegCommand("涂鸦", ""),
//        FFmpegCommand("倒放", ""),
//        FFmpegCommand("素描", ""),
//        FFmpegCommand("色彩平衡", ""),
//        FFmpegCommand("模糊", ""),
//        FFmpegCommand("九宫格", ""),
//        FFmpegCommand("添加贴纸", ""),
//        FFmpegCommand("滤镜", ""),
//        FFmpegCommand("分屏", "")
//    )
    private var mSampleSelectedIndex: Int = -1

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
    }

    private var inputPath: String? = null
    private var outputPath: String? = null

    private fun initFFmpegCommond() {
        //FFmpegKitConfig.setFontDirectory()
        SAMPLE_TITLES = ArrayList<FFmpegCommand>()
        inputPath = mVideoPath
        outputPath = cacheDir.absolutePath + File.separator + FileUtils.getFileName(inputPath)
            .endsWith(".") + "_ffmpeg.mp4"

        SAMPLE_TITLES.add(
            FFmpegCommand(
                "文字水印",
                "-i $inputPath -vf \"drawtext=fontfile=/sdcard/simsun.ttc:text='I Love You, 复仇者联盟：终局之战':fontsize=24:fontcolor=red:x=20:y=20:shadowy=2:\" -vcodec libx264 $outputPath -y"
            )
        )


        val bitmap = BitmapUtils.getBitmap(R.mipmap.ic_launcher)
        val waterMarkPath =
            cacheDir.absolutePath + File.separator + System.currentTimeMillis() + ".png"
        BitmapUtils.save(bitmap, waterMarkPath, Bitmap.CompressFormat.PNG)
        SAMPLE_TITLES.add(
            FFmpegCommand(
                "图片水印",
                "-i $inputPath -vf \"movie=$waterMarkPath[watermark];[in][watermark]overlay=20:20\" -vcodec libx264 $outputPath -y"
            )
        )

        SAMPLE_TITLES.add(
            FFmpegCommand(
                "图片+文字水印",
                "-i $inputPath -vf \"[in]drawtext=fontfile=/sdcard/simsun.ttc:text='I Love You, 复仇者联盟：终局之战':fontsize=24:fontcolor=red:x=20:y=20:shadowy=2[text];movie=$waterMarkPath[watermark];[text][watermark]overlay=W-w:H-h[out]\" -vcodec libx264 $outputPath -y"
            )
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 1000) {
            if (data != null) {
                val list: List<MediaEntity> = MediaPicker.obtainMediaResults(data)
                val one = list[0]
                initVideoView(one.path)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_ffmpeg_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (StringUtils.isTrimEmpty(mVideoPath)) {
            Toast.makeText(this, "请先选择视频", Toast.LENGTH_SHORT).show()
            return super.onOptionsItemSelected(item)
        }
        val id = item.itemId
        if (id == R.id.action_select) {
            showFFmpegDialog()
        } else if (id == R.id.action_info) {
            showMediaInfo()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showMediaInfo() {

        val build = SuperDialog.CommonBuilder(this)
            .setContentView(R.layout.dialog_media_info)
            .setCancelable(true)
            .setCanceledOnTouchOutside(true)
            .setGravity(Gravity.CENTER)
            .setDefaultAnim()
            .setWidthRatio(0.8)
            .setHeightRatio(0.6)
            .setText(R.id.tv_media_info, buildMediaInfo())
            .build()
        build.show()
    }

    private fun buildMediaInfo(): CharSequence? {
        val sb = StringBuilder()
        val mediaInformation = FFprobeKit.getMediaInformation(mVideoPath)
        sb.append(StreamInformation.KEY_NB_FRAMES+": "+mediaInformation.mediaInformation.streams[0].getStringProperty(StreamInformation.KEY_NB_FRAMES))

        return sb.toString()
    }

    private fun initVideoView(videoPath: String) {
        mVideoPath = videoPath
        mDataBinding.videoView.setVideoPath(videoPath)
        mDataBinding.videoView.setMediaController(MediaController(this))
        mDataBinding.videoView.start()

        initFFmpegCommond()
    }

    private fun showFFmpegDialog() {

        val build = SuperDialog.CommonBuilder(this)
            .setContentView(R.layout.dialog_sample_selected_layout)
            .setCancelable(true)
            .setCanceledOnTouchOutside(true)
            .setGravity(Gravity.CENTER)
            .setDefaultAnim()
            .setWidthRatio(0.8)
            .setHeightRatio(0.6)
            .build()
        build.show()

        val builder = AlertDialog.Builder(this)
        val inflater = LayoutInflater.from(this)
        val rootView: View = inflater.inflate(R.layout.dialog_sample_selected_layout, null)

        val dialog = builder.create()

        val resolutionsListView: RecyclerView = rootView.findViewById(R.id.resolution_list_view)

        val myPreviewSizeViewAdapter =
            FFmpegRecyclerViewAdapter(this, SAMPLE_TITLES)
        myPreviewSizeViewAdapter.setSelectIndex(mSampleSelectedIndex)
        myPreviewSizeViewAdapter.addOnItemClickListener { view, position ->
            myPreviewSizeViewAdapter.selectIndex = position
            mSampleSelectedIndex = position
            dialog.cancel()
            dealVideo()
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
        FFmpegKit.executeAsync(SAMPLE_TITLES[mSampleSelectedIndex].cmd, this, this, this)
    }

    override fun apply(session: FFmpegSession?) {
        runOnUiThread {
            hideLoading()
            Toast.makeText(this, session?.returnCode.toString(), Toast.LENGTH_SHORT).show()
            mDataBinding.videoView.pause()
            mDataBinding.videoView.setVideoPath(outputPath)
            mDataBinding.videoView.start()
        }
    }

    override fun apply(statistics: Statistics?) {
        Logger.i(statistics.toString())
    }

    override fun apply(FFmpegLog: FFmpegLog?) {
        Log.i("ffmpegLibrary422-j-log", FFmpegLog.toString())
    }

}