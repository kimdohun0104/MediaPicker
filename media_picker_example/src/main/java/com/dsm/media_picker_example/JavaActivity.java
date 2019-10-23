package com.dsm.media_picker_example;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.dsm.mediapicker.MediaPicker;

public class JavaActivity extends AppCompatActivity {

    private static final int FRAGMENT_EXAMPLE_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_java);

        findViewById(R.id.btn_start).setOnClickListener(v ->
                MediaPicker.Companion.createImage(this)
                        .start(100));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FRAGMENT_EXAMPLE_CODE) {
            if (resultCode == RESULT_OK) {
                Log.d("FRAGMENT_RESULT", MediaPicker.Companion.getResult(data).toString());
            }
        }
    }
}
