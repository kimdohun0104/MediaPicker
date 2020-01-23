package com.dsm.media_picker_example

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.dsm.mediapicker.MediaPicker
import kotlinx.android.synthetic.main.activity_callback.*

class CallbackActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_callback)

        btn_start.setOnClickListener {
            MediaPicker.createImage(this)
                .onResult { Log.d("CALLBACK_RESULT", it.toString()) }
                .start()
        }
    }
}
