package com.dsm.mediapicker.config

import android.os.Parcelable
import com.dsm.mediapicker.enum.PickerOrientation
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ImageConfig(
    var maxImageCount: Int = DefaultConfig.MAX_IMAGE_COUNT,

    var toolbarTitle: String = DefaultConfig.TOOLBAR_TITLE,
    var toolbarCompleteText: String = DefaultConfig.TOOLBAR_COMPLETE_TEXT,
    var toolbarBackgroundColor: Int = DefaultConfig.TOOLBAR_BACKGROUND_COLOR,
    var toolbarTextColor: Int = DefaultConfig.TOOLBAR_TEXT_COLOR,

    var portraitSpan: Int = DefaultConfig.PORTRAIT_SPAN,
    var landscapeSpan: Int = DefaultConfig.LANDSCAPE_SPAN,

    var orientation: PickerOrientation = DefaultConfig.ORIENTATION,
    var theme: Int = DefaultConfig.THEME
) : Parcelable