package com.dsm.mediapicker.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dsm.mediapicker.R;
import com.dsm.mediapicker.config.ImageConfig;
import com.dsm.mediapicker.enums.PickerOrientation;
import com.dsm.mediapicker.listener.MediaPickerListener;
import com.dsm.mediapicker.util.ImageUtil;
import com.dsm.mediapicker.util.PermissionUtil;

import java.util.ArrayList;
import java.util.List;

public class ImagePickActivity extends AppCompatActivity {

    public static final String IMAGE_CONFIG = "IMAGE_CONFIG";
    public static final int EXTERNAL_STORAGE = 100;

    public static void startActivity(Context context, Intent intent, MediaPickerListener newListener) {
        context.startActivity(intent);
        listener = newListener;
    }

    private static MediaPickerListener listener;
    private ImageConfig config;

    private Toolbar tbImage;
    private TextView tvTitle;
    private TextView tvSelect;
    private LinearLayout llCount;
    private TextView tvCurrent;
    private TextView tvMax;
    private TextView tvSlash;
    private RecyclerView rvImage;

    private ImageListAdapter adapter;

    @Override
    public Resources.Theme getTheme() {
        config = (ImageConfig) getIntent().getSerializableExtra(IMAGE_CONFIG);
        if (config == null) {
            throw new IllegalStateException("ImageConfig can not be null! If it's happened, please leave a issue!");
        }

        Resources.Theme theme = super.getTheme();
        theme.applyStyle(config.getTheme(), true);
        return theme;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED)
                Toast.makeText(this, config.getPermissionRequireText(), Toast.LENGTH_SHORT).show();
            else
                adapter.setImages(ImageUtil.getAllShownImagesPath(this));
        } else super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_pick);

        if (!PermissionUtil.isReadExternalStorageAllow(this))
            PermissionUtil.requestReadExternalStorage(this, EXTERNAL_STORAGE);

        setupOrientation();
        setupFindViewById();
        setupTextAndColor();
        setupSingleMode();
        setupRecyclerView();
        setupSelect();
    }

    private void setupOrientation() {
        if (config.getOrientation() == PickerOrientation.PORTRAIT)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        else if (config.getOrientation() == PickerOrientation.LANDSCAPE)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    private void setupFindViewById() {
        tbImage = findViewById(R.id.tb_image);
        tvTitle = findViewById(R.id.tv_title);
        tvSelect = findViewById(R.id.tv_select);
        llCount = findViewById(R.id.ll_count);
        tvCurrent = findViewById(R.id.tv_current);
        tvMax = findViewById(R.id.tv_max);
        tvSlash = findViewById(R.id.tv_slash);
        rvImage = findViewById(R.id.rv_image);
    }

    private void setupTextAndColor() {
        tvTitle.setText(config.getToolbarTitle());
        tvSelect.setText(config.getToolbarSelectText());
        tbImage.setTitle(config.getToolbarTitle());
        tvCurrent.setText("0");
        tvMax.setText(String.valueOf(config.getMaxImageCount()));

        tvTitle.setTextColor(getColorFromId(config.getToolbarTextColor()));
        tbImage.setBackgroundResource(config.getToolbarBackgroundColor());
        tbImage.setTitleTextColor(config.getToolbarTextColor());
        tvSelect.setTextColor(getColorFromId(config.getToolbarTextColor()));
        tvCurrent.setTextColor(getColorFromId(config.getToolbarTextColor()));
        tvMax.setTextColor(getColorFromId(config.getToolbarTextColor()));
        tvSlash.setTextColor(getColorFromId(config.getToolbarTextColor()));
    }

    private void setupSingleMode() {
        boolean isSingle = config.getMaxImageCount() == 1;

        if (isSingle) {
            llCount.setVisibility(View.INVISIBLE);
        } else {
            llCount.setVisibility(View.VISIBLE);
        }
    }

    private void setupRecyclerView() {
        GridLayoutManager layoutManager = (GridLayoutManager) rvImage.getLayoutManager();
        if (isPortrait() && layoutManager != null) {
            layoutManager.setSpanCount(config.getPortraitSpan());
        } else if (layoutManager != null) {
            layoutManager.setSpanCount(config.getLandscapeSpan());
        }

        adapter = new ImageListAdapter(config.getMaxImageCount(), (selectedCount) -> {
            tvCurrent.setText(String.valueOf(selectedCount));
        });
        rvImage.setAdapter(adapter);

        if (PermissionUtil.isReadExternalStorageAllow(this))
            adapter.setImages(ImageUtil.getAllShownImagesPath(this));
    }

    private void setupSelect() {
        tvSelect.setOnClickListener(v -> {
            ArrayList<String> realPaths = new ArrayList<>();

            if (adapter.getSelectedUris().size() == 0) return;

            for (Uri uri : adapter.getSelectedUris()) {
                realPaths.add(ImageUtil.getRealPathFromUri(this, uri));
            }

            if (listener != null) {
                listener.onSelected(realPaths);
            } else {
                Intent intent = new Intent();
                intent.putStringArrayListExtra("result", realPaths);
                setResult(RESULT_OK, intent);
            }
            finish();
        });
    }

    private int getColorFromId(int colorId) {
        return ContextCompat.getColor(this, colorId);
    }

    private boolean isPortrait() {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }
}
