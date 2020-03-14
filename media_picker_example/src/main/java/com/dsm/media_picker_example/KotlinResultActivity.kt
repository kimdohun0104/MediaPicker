package com.dsm.media_picker_example

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.dsm.mediapicker.MediaPicker
import kotlinx.android.synthetic.main.activity_kotlin_result.*

class KotlinResultActivity : AppCompatActivity() {

    companion object {
        private const val KOTLIN_RESULT_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin_result)

        btn_kotlin_result.setOnClickListener {
            MediaPicker.withContext(this)
                .permissionRequireText(R.string.permission)
                .start(KOTLIN_RESULT_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == KOTLIN_RESULT_CODE && resultCode == RESULT_OK) {
            Log.d("KOTLIN_RESULT", MediaPicker.getResults(data).toString())
        }
    }
}