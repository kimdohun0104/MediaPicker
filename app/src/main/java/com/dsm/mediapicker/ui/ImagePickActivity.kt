package com.dsm.mediapicker.ui

import android.app.Activity
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.content.res.Resources
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dsm.mediapicker.R
import com.dsm.mediapicker.callback.OnImageSelected
import com.dsm.mediapicker.config.DefaultConfig
import com.dsm.mediapicker.config.ImageConfig
import com.dsm.mediapicker.enum.PickerOrientation
import kotlinx.android.synthetic.main.activity_image_pick.*

class ImagePickActivity : AppCompatActivity() {

    private val config: ImageConfig by lazy { intent.extras?.getParcelable(ImageConfig::class.java.simpleName) ?: ImageConfig() }
    private val adapter: ImageListAdapter by lazy {
        ImageListAdapter(this, config.maxImageCount, object : OnImageSelected {
            override fun onSelected(count: Int) {
                tv_current.text = count.toString()
            }
        })
    }

    override fun getTheme(): Resources.Theme {
        val theme = super.getTheme()
        theme.applyStyle(config.theme, true)
        return theme
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_pick)

        if (config.orientation == PickerOrientation.PORTRAIT)
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        else if (config.orientation == PickerOrientation.LANDSCAPE)
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        viewInit()
        recyclerViewInit()
    }

    private fun viewInit() {
        tv_title.text = config.toolbarTitle
        tv_complete.text = config.toolbarCompleteText
        tb_image.title = config.toolbarTitle
        tv_current.text = "0"
        tv_max.text = config.maxImageCount.toString()

        tb_image.setBackgroundResource(config.toolbarBackgroundColor)
        tb_image.setTitleTextColor(config.toolbarTextColor)
        tv_complete.setTextColor(getColorFromId(config.toolbarTextColor))
        tv_current.setTextColor(getColorFromId(config.toolbarTextColor))
        tv_max.setTextColor(getColorFromId(config.toolbarTextColor))
        tv_slash.setTextColor(getColorFromId(config.toolbarTextColor))

        if (config.maxImageCount == DefaultConfig.MAX_IMAGE_COUNT) {
            tv_slash.visibility = View.GONE
            tv_max.visibility = View.GONE
        }

        tv_complete.setOnClickListener {
            config.onComplete?.let { onSelected ->
                onSelected.onComplete(adapter.getSelectedPath())
                finish()
            }
        }
    }

    private fun getColorFromId(colorId: Int) = ContextCompat.getColor(this, colorId)

    private fun recyclerViewInit() {
        val layoutManager: RecyclerView.LayoutManager =
            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
                GridLayoutManager(this, config.portraitSpan)
            else
                GridLayoutManager(this, config.landscapeSpan)

        rv_image.adapter = adapter
        rv_image.layoutManager = layoutManager
        adapter.addItems(getAllShownImagesPath(this))
    }

    private fun getAllShownImagesPath(activity: Activity): List<Uri> {
        val uriExternal: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val cursor: Cursor?
        val columnIndexID: Int
        val listOfAllImages = arrayListOf<Uri>()
        val projection = arrayOf(MediaStore.Images.Media._ID)
        var imageId: Long
        cursor = activity.contentResolver.query(uriExternal, projection, null, null, null)
        if (cursor != null) {
            columnIndexID = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            while (cursor.moveToNext()) {
                imageId = cursor.getLong(columnIndexID)
                val uriImage = Uri.withAppendedPath(uriExternal, "" + imageId)
                listOfAllImages.add(uriImage)
            }
            cursor.close()
        }
        return listOfAllImages
    }
}
