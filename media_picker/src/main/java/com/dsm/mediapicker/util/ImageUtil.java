package com.dsm.mediapicker.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

public class ImageUtil {

    public static List<Uri> getAllShownImagesPath(Activity activity) {
        Uri uriExternal = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ArrayList<Uri> listOfAllImages = new ArrayList<>();
        String[] projection = new String[]{MediaStore.Images.Media._ID};
        Cursor cursor = activity.getContentResolver().query(uriExternal, projection, null, null, MediaStore.Images.ImageColumns.DATE_ADDED + " DESC");
        if (cursor != null) {
            int columnIndexId = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
            long imageId;
            while (cursor.moveToNext()) {
                imageId = cursor.getLong(columnIndexId);
                Uri uriImage = Uri.withAppendedPath(uriExternal, String.valueOf(imageId));
                listOfAllImages.add(uriImage);
            }
            cursor.close();
        }
        return listOfAllImages;
    }

    public static String getRealPathFromUri(Activity activity, Uri contentUri) {
        String path = null;
        String[] proj = {MediaStore.MediaColumns.DATA};
        Cursor cursor = activity.getContentResolver().query(contentUri, proj, null, null, null);
        assert cursor != null;
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            path = cursor.getString(column_index);
        }
        cursor.close();
        return path;
    }
}