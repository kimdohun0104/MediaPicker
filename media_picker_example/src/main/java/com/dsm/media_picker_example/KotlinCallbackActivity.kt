package com.dsm.media_picker_example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.dsm.mediapicker.MediaPicker
import kotlinx.android.synthetic.main.activity_kotlin_callback.*

class KotlinCallbackActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin_callback)

        btn_kotlin_callback.setOnClickListener {
            MediaPicker.withContext(this)
                .single()
                .toolbarTitle(R.string.toolbar_title)
                .start {
                    Log.d("KOTLIN_CALLBACK", it.toString())
                }
        }
    }
}