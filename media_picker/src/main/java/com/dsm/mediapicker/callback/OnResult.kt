package com.dsm.mediapicker.callback

import java.io.Serializable

data class OnResult(
    val result: (ArrayList<String>) -> Unit
) : Serializable