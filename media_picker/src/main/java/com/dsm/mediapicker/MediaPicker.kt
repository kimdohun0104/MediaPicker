package com.dsm.mediapicker

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.annotation.ColorRes
import androidx.annotation.StyleRes
import androidx.fragment.app.Fragment
import com.dsm.mediapicker.config.DefaultConfig
import com.dsm.mediapicker.config.ImageConfig
import com.dsm.mediapicker.enum.PickerOrientation
import com.dsm.mediapicker.ui.ImagePickActivity

abstract class MediaPicker {

    private val imageConfig: ImageConfig by lazy { ImageConfig() }

    abstract fun start(requestCode: Int)

    companion object {
        class ImagePickerWithActivity(private val activity: Activity) : MediaPicker() {
            override fun start(requestCode: Int) =
                activity.startActivityForResult(getImageIntent(activity), requestCode)
        }

        class ImagePickerWithFragment(private val fragment: Fragment) : MediaPicker() {
            override fun start(requestCode: Int) =
                fragment.startActivityForResult(getImageIntent(fragment.context!!), requestCode)
        }

        fun createImage(activity: Activity) = ImagePickerWithActivity(activity)

        fun createImage(fragment: Fragment) = ImagePickerWithFragment(fragment)

        fun getResult(intent: Intent?): List<String> =
            intent?.getStringArrayListExtra("result")!!.toList()
    }

    fun getImageIntent(context: Context): Intent =
        Intent(context, ImagePickActivity::class.java).apply {
            putExtra(ImageConfig::class.java.simpleName, imageConfig)
        }


    fun single(): MediaPicker {
        imageConfig.maxImageCount = 1
        return this
    }

    fun maxImageCount(count: Int): MediaPicker {
        if (count < 0)
            imageConfig.maxImageCount = DefaultConfig.MAX_IMAGE_COUNT
        imageConfig.maxImageCount = count
        return this
    }

    fun toolbarTitle(title: String): MediaPicker {
        imageConfig.toolbarTitle = title
        return this
    }

    fun toolbarCompleteText(completeText: String): MediaPicker {
        imageConfig.toolbarCompleteText = completeText
        return this
    }

    fun toolbarBackgroundColor(@ColorRes backgroundColor: Int): MediaPicker {
        imageConfig.toolbarBackgroundColor = backgroundColor
        return this
    }

    fun toolbarTextColor(@ColorRes textColor: Int): MediaPicker {
        imageConfig.toolbarTextColor = textColor
        return this
    }

    fun portraitSpan(portraitSpan: Int): MediaPicker {
        imageConfig.portraitSpan = portraitSpan
        return this
    }

    fun landscapeSpan(landscapeSpan: Int): MediaPicker {
        imageConfig.landscapeSpan = landscapeSpan
        return this
    }

    fun orientation(orientation: PickerOrientation): MediaPicker {
        imageConfig.orientation = orientation
        return this
    }

    fun theme(@StyleRes theme: Int): MediaPicker {
        imageConfig.theme = theme
        return this
    }
}