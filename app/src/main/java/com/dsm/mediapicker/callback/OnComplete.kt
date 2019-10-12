package com.dsm.mediapicker.callback

import java.io.Serializable

class OnComplete(val onComplete: (List<String>) -> Unit) : Serializable