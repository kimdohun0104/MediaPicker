package com.dsm.media_picker_example;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.dsm.mediapicker.MediaPicker;

public class JavaResultActivity extends AppCompatActivity {

    private static final int PICKER_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_java_result);

        findViewById(R.id.btn_java_result).setOnClickListener(v -> MediaPicker.withContext(this)
                .toolbarTextColor(R.color.colorPrimary)
                .single()
                .start(PICKER_CODE));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICKER_CODE && resultCode == RESULT_OK) {
            Log.d("JAVA_RESULT", String.valueOf(MediaPicker.getResults(data)));
        }
    }
}