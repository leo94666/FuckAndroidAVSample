package com.top.av.ui

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.*
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.top.arch.base.BaseActivity
import com.top.av.R
import com.top.av.adapter.OpenGLRecyclerViewAdapter
import com.top.av.databinding.ActivityOpenGlBinding

class OpenGLESActivity: BaseActivity<ActivityOpenGlBinding>() {

    val SAMPLE_TITLES = mutableListOf("DrawTriangle","Video")
    private val mSampleSelectedIndex: Int = 0

    companion object {
        fun start(context: Context) {
            var intent = Intent(context, OpenGLESActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun getLayout(): Int {
        return R.layout.activity_open_gl
    }

    override fun hideTitleBar() {
        //super.hideTitleBar()
    }
    override fun init(root: View?) {

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_opengl_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_change_sample) {
            showGLSampleDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showGLSampleDialog() {

        val builder = AlertDialog.Builder(this)
        val inflater = LayoutInflater.from(this)
        val rootView: View = inflater.inflate(R.layout.dialog_sample_selected_layout, null)

        val dialog = builder.create()


        val resolutionsListView: RecyclerView = rootView.findViewById(R.id.resolution_list_view)


        val myPreviewSizeViewAdapter =
            OpenGLRecyclerViewAdapter(this, SAMPLE_TITLES)
        myPreviewSizeViewAdapter.setSelectIndex(mSampleSelectedIndex)
        myPreviewSizeViewAdapter.addOnItemClickListener { view, position ->
            dialog.cancel()
        }
        val manager = LinearLayoutManager(this)
        manager.orientation = LinearLayoutManager.VERTICAL
        resolutionsListView.layoutManager = manager

        resolutionsListView.adapter = myPreviewSizeViewAdapter
        resolutionsListView.scrollToPosition(mSampleSelectedIndex)

        dialog.show()
        dialog.window!!.setContentView(rootView)
    }
}