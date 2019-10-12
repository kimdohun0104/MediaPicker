package com.dsm.mediapicker.config

import com.dsm.mediapicker.R
import com.dsm.mediapicker.enum.PickerOrientation

object DefaultConfig {
    const val MAX_IMAGE_COUNT = 999

    const val TOOLBAR_TITLE = "Pick Image"
    const val TOOLBAR_COMPLETE_TEXT = "Complete"
    val TOOLBAR_BACKGROUND_COLOR = R.color.colorPickerBlue
    val TOOLBAR_TEXT_COLOR = R.color.colorPickerWhite

    const val PORTRAIT_SPAN = 3
    const val LANDSCAPE_SPAN = 5

    val ORIENTATION = PickerOrientation.BOTH
    val THEME = R.style.PickerTheme
}