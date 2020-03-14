package com.dsm.media_picker_example;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.dsm.mediapicker.MediaPicker;

public class JavaCallbackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_java_callback);

        findViewById(R.id.btn_start).setOnClickListener(v ->
                MediaPicker.withContext(this)
                        .maxImageCount(5)
                        .toolbarBackgroundColor(R.color.colorAccent)
                        .toolbarTextColor(R.color.colorMediaPickerBlue)
                        .start((list) -> Log.d("JAVA_CALLBACK_RESULT", String.valueOf(list))));
    }
}
