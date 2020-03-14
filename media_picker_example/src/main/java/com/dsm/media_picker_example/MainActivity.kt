package com.dsm.media_picker_example

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_java_callback.setOnClickListener {
            startActivity(Intent(this, JavaCallbackActivity::class.java))
        }

        btn_java_result.setOnClickListener {
            startActivity(Intent(this, JavaResultActivity::class.java))
        }

        btn_kotlin_callback.setOnClickListener {
            startActivity(Intent(this, KotlinCallbackActivity::class.java))
        }

        btn_kotlin_result.setOnClickListener {
            startActivity(Intent(this, KotlinResultActivity::class.java))
        }
    }
}
