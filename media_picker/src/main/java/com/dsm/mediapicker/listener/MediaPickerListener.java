package com.dsm.mediapicker.listener;

import java.util.List;

@FunctionalInterface
public interface MediaPickerListener {

    void onSelected(List<String> paths);
}
