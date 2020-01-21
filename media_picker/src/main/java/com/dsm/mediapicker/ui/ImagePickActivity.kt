package com.dsm.mediapicker.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.Resources
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dsm.mediapicker.R
import com.dsm.mediapicker.config.DefaultConfig
import com.dsm.mediapicker.config.ImageConfig
import com.dsm.mediapicker.enum.PickerOrientation
import kotlinx.android.synthetic.main.activity_image_pick.*

class ImagePickActivity : AppCompatActivity() {

    companion object {
        private const val READ_EXTERNAL_STORAGE_CODE = 5254
    }

    private val config: ImageConfig by lazy { intent.extras?.getParcelable(ImageConfig::class.java.simpleName) ?: ImageConfig() }
    private val adapter: ImageListAdapter by lazy {
        ImageListAdapter(this, config.maxImageCount) { tv_current.text = it.toString() }
    }

    override fun getTheme(): Resources.Theme {
        val theme = super.getTheme()
        theme.applyStyle(config.theme, true)
        return theme
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_pick)
        requestPermission()

        if (config.orientation == PickerOrientation.PORTRAIT)
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        else if (config.orientation == PickerOrientation.LANDSCAPE)
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        viewInit()
        recyclerViewInit()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            READ_EXTERNAL_STORAGE_CODE -> {
                if (grantResults[0] == PackageManager.PERMISSION_DENIED)
                    Toast.makeText(this, R.string.require_permission_to_pick, Toast.LENGTH_SHORT).show()
                else
                    adapter.imageList = getAllShownImagesPath(this)
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun requestPermission() =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), READ_EXTERNAL_STORAGE_CODE)
        else Unit // Can not ask runtime permission below 6.0

    private fun isReadExternalStorageAllow(): Boolean =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_DENIED
        else true

    private fun viewInit() {
        tv_title.text = config.toolbarTitle
        tv_complete.text = config.toolbarCompleteText
        tb_image.title = config.toolbarTitle
        tv_current.text = "0"
        tv_max.text = config.maxImageCount.toString()

        tv_title.setTextColor(getColorFromId(config.toolbarTextColor))
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
            val selectedList = adapter.getSelectedPath()

            config.onResult?.result?.invoke(selectedList)

            val resultIntent = Intent().apply { putStringArrayListExtra("result", selectedList) }
            setResult(RESULT_OK, resultIntent)
            finish()
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

        isReadExternalStorageAllow()
    }

    private fun getAllShownImagesPath(activity: Activity): List<Uri> {
        val uriExternal: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val listOfAllImages = arrayListOf<Uri>()
        val projection = arrayOf(MediaStore.Images.Media._ID)
        val cursor = activity.contentResolver.query(uriExternal, projection, null, null, MediaStore.Images.ImageColumns.DATE_ADDED + " DESC")
        cursor?.let {
            val columnIndexID = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            var imageId: Long
            while (cursor.moveToNext()) {
                imageId = cursor.getLong(columnIndexID)
                val uriImage = Uri.withAppendedPath(uriExternal, imageId.toString())
                listOfAllImages.add(uriImage)
            }
            cursor.close()
        }
        return listOfAllImages
    }
}
