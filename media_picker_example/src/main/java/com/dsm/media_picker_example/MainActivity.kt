package com.dsm.media_picker_example

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.dsm.mediapicker.MediaPicker
import com.dsm.mediapicker.enum.PickerOrientation
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_start.setOnClickListener {
            MediaPicker.createImage(this)
                .orientation(PickerOrientation.PORTRAIT)
                .landscapeSpan(4)
                .portraitSpan(2)
                .toolbarTitle("Hello")
                .start {
                    Log.d("DEBUGLOG", it.toString()) // get selected images real path
                }
        }
    }
}
