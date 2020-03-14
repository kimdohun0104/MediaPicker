package com.dsm.mediapicker.config;

import com.dsm.mediapicker.R;
import com.dsm.mediapicker.enums.PickerOrientation;

import java.io.Serializable;


public class ImageConfig implements Serializable {

    private int maxImageCount = 10;

    private String toolbarTitle = "Pick Image";
    private String toolbarSelectText = "Select";
    private int toolbarBackgroundColor = R.color.colorMediaPickerBlue;
    private int toolbarTextColor = R.color.colorMediaPickerWhite;

    private int portraitSpan = 3;
    private int landscapeSpan = 5;

    private int orientation = PickerOrientation.BOTH;
    private int theme = R.style.MediaPickerDefaultTheme;

    private String permissionRequireText = "You need permission to do this";

    public int getMaxImageCount() {
        return maxImageCount;
    }

    public void setMaxImageCount(int maxImageCount) {
        this.maxImageCount = maxImageCount;
    }

    public String getToolbarTitle() {
        return toolbarTitle;
    }

    public void setToolbarTitle(String toolbarTitle) {
        this.toolbarTitle = toolbarTitle;
    }

    public String getToolbarSelectText() {
        return toolbarSelectText;
    }

    public void setToolbarSelectText(String toolbarSelectText) {
        this.toolbarSelectText = toolbarSelectText;
    }

    public int getToolbarBackgroundColor() {
        return toolbarBackgroundColor;
    }

    public void setToolbarBackgroundColor(int toolbarBackgroundColor) {
        this.toolbarBackgroundColor = toolbarBackgroundColor;
    }

    public int getToolbarTextColor() {
        return toolbarTextColor;
    }

    public void setToolbarTextColor(int toolbarTextColor) {
        this.toolbarTextColor = toolbarTextColor;
    }

    public int getPortraitSpan() {
        return portraitSpan;
    }

    public void setPortraitSpan(int portraitSpan) {
        this.portraitSpan = portraitSpan;
    }

    public int getLandscapeSpan() {
        return landscapeSpan;
    }

    public void setLandscapeSpan(int landscapeSpan) {
        this.landscapeSpan = landscapeSpan;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public int getTheme() {
        return theme;
    }

    public void setTheme(int theme) {
        this.theme = theme;
    }

    public String getPermissionRequireText() {
        return permissionRequireText;
    }

    public void setPermissionRequireText(String permissionRequireText) {
        this.permissionRequireText = permissionRequireText;
    }
}
