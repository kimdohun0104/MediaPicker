package com.dsm.mediapicker.config

import android.os.Parcel
import android.os.Parcelable
import com.dsm.mediapicker.callback.OnComplete

class ImageConfig() : Parcelable {

    constructor(parcel: Parcel) : this() {
        maxImageCount = parcel.readInt()
        onComplete = parcel.readSerializable() as OnComplete
        toolbarTitle = parcel.readString() ?: DefaultConfig.TOOLBAR_TITLE
        toolbarCompleteText = parcel.readString() ?: DefaultConfig.TOOLBAR_COMPLETE_TEXT
        toolbarBackgroundColor = parcel.readInt()
        toolbarTextColor = parcel.readInt()
        portraitSpan = parcel.readInt()
        landscapeSpan = parcel.readInt()
        orientation = parcel.readInt()
        theme = parcel.readInt()
    }

    companion object CREATOR : Parcelable.Creator<ImageConfig> {
        override fun createFromParcel(parcel: Parcel): ImageConfig = ImageConfig(parcel)

        override fun newArray(size: Int): Array<ImageConfig?> = arrayOfNulls(size)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(maxImageCount)
        parcel.writeSerializable(onComplete)
        parcel.writeString(toolbarTitle)
        parcel.writeString(toolbarCompleteText)
        parcel.writeInt(toolbarBackgroundColor)
        parcel.writeInt(toolbarTextColor)
        parcel.writeInt(portraitSpan)
        parcel.writeInt(landscapeSpan)
        parcel.writeInt(orientation)
        parcel.writeInt(theme)
    }

    override fun describeContents(): Int = 0

    var maxImageCount: Int = DefaultConfig.MAX_IMAGE_COUNT

    var onComplete: OnComplete? = null

    var toolbarTitle: String = DefaultConfig.TOOLBAR_TITLE
    var toolbarCompleteText: String = DefaultConfig.TOOLBAR_COMPLETE_TEXT
    var toolbarBackgroundColor: Int = DefaultConfig.TOOLBAR_BACKGROUND_COLOR
    var toolbarTextColor: Int = DefaultConfig.TOOLBAR_TEXT_COLOR

    var portraitSpan: Int = DefaultConfig.PORTRAIT_SPAN
    var landscapeSpan: Int = DefaultConfig.LANDSCAPE_SPAN

    var orientation: Int = DefaultConfig.ORIENTATION
    var theme: Int = DefaultConfig.THEME
}