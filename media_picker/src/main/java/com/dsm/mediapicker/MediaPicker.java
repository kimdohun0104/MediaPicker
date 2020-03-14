package com.dsm.mediapicker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import androidx.annotation.StyleRes;

import com.dsm.mediapicker.config.ImageConfig;
import com.dsm.mediapicker.listener.MediaPickerListener;
import com.dsm.mediapicker.ui.ImagePickActivity;

import java.util.Collections;
import java.util.List;

public class MediaPicker {

    public static MediaPicker withContext(Context context) {
        return new MediaPicker(context);
    }

    private Context context;
    private ImageConfig imageConfig = new ImageConfig();

    private MediaPicker(Context context) {
        this.context = context;
    }

    public void start(int requestCode) {
        ((Activity) context).startActivityForResult(getIntent(), requestCode);
    }

    public void start(MediaPickerListener mediaPickerListener) {
        if (mediaPickerListener == null) {
            throw new IllegalArgumentException("OnImageSelectedListener must not be null");
        }

        ImagePickActivity.startActivity(context, getIntent(), mediaPickerListener);
    }

    public static List<String> getResults(Intent intent) {
        if (intent != null) {
            return intent.getStringArrayListExtra("result");
        } else {
            return Collections.emptyList();
        }
    }

    private Intent getIntent() {
        Intent intent = new Intent(context, ImagePickActivity.class);
        intent.putExtra(ImagePickActivity.IMAGE_CONFIG, imageConfig);
        return intent;
    }

    public MediaPicker single() {
        imageConfig.setMaxImageCount(1);
        return this;
    }

    public MediaPicker maxImageCount(int maxImageCount) {
        imageConfig.setMaxImageCount(maxImageCount);
        return this;
    }

    public MediaPicker toolbarTitle(String toolbarTitle) {
        imageConfig.setToolbarTitle(toolbarTitle);
        return this;
    }

    public MediaPicker toolbarTitle(@StringRes int toolbarTitle) {
        imageConfig.setToolbarTitle(context.getString(toolbarTitle));
        return this;
    }

    public MediaPicker toolbarSelectText(String toolbarSelectText) {
        imageConfig.setToolbarSelectText(toolbarSelectText);
        return this;
    }

    public MediaPicker toolbarSelectText(@StringRes int toolbarSelectText) {
        imageConfig.setToolbarSelectText(context.getString(toolbarSelectText));
        return this;
    }

    public MediaPicker toolbarBackgroundColor(@DrawableRes int toolbarBackgroundColor) {
        imageConfig.setToolbarBackgroundColor(toolbarBackgroundColor);
        return this;
    }

    public MediaPicker toolbarTextColor(@ColorRes int toolbarTextColor) {
        imageConfig.setToolbarTextColor(toolbarTextColor);
        return this;
    }

    public MediaPicker portraitSpan(int portraitSpan) {
        imageConfig.setPortraitSpan(portraitSpan);
        return this;
    }

    public MediaPicker landscapeSpan(int landscapeSpan) {
        imageConfig.setLandscapeSpan(landscapeSpan);
        return this;
    }

    public MediaPicker orientation(int orientation) {
        imageConfig.setOrientation(orientation);
        return this;
    }

    public MediaPicker theme(@StyleRes int theme) {
        imageConfig.setTheme(theme);
        return this;
    }

    public MediaPicker permissionRequireText(String permissionRequireText) {
        imageConfig.setPermissionRequireText(permissionRequireText);
        return this;
    }

    public MediaPicker permissionRequireText(@StringRes int permissionRequireText) {
        imageConfig.setPermissionRequireText(context.getString(permissionRequireText));
        return this;
    }
}
