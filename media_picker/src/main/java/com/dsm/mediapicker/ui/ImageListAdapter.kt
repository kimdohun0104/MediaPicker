package com.dsm.mediapicker.ui

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dsm.mediapicker.R
import com.dsm.mediapicker.callback.OnImageSelected
import kotlinx.android.synthetic.main.item_image.view.*

class ImageListAdapter(
    private val context: Context,
    private val maxCount: Int,
    private val onImageSelected: OnImageSelected
) : RecyclerView.Adapter<ImageListAdapter.ImageViewHolder>() {

    private val imageList = arrayListOf<Uri>()
    private val selectedPosition = arrayListOf<Int>()

    private var originalSelectedView: View? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder =
        ImageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false))

    override fun getItemCount(): Int = imageList.size

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) = holder.bind()

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind() {
            Glide.with(itemView.context).load(imageList[adapterPosition]).into(itemView.iv_image)
            if (selectedPosition.contains(adapterPosition)) {
                itemView.ll_check.visibility = View.VISIBLE
            } else {
                itemView.ll_check.visibility = View.INVISIBLE
            }
            itemView.setOnClickListener {
                if (selectedPosition.contains(adapterPosition)) {
                    selectedPosition.remove(adapterPosition)
                    itemView.ll_check.visibility = View.GONE
                    onImageSelected.onSelected(selectedPosition.size)
                } else {
                    // single mode
                    if (maxCount == 1) {
                        originalSelectedView?.ll_check?.visibility = View.INVISIBLE
                        originalSelectedView = it
                        selectedPosition.clear()
                        selectedPosition.add(adapterPosition)
                        itemView.ll_check.visibility = View.VISIBLE
                        onImageSelected.onSelected(selectedPosition.size)
                    } else {
                        if (selectedPosition.size == maxCount)
                            return@setOnClickListener
                        selectedPosition.add(adapterPosition)
                        itemView.ll_check.visibility = View.VISIBLE
                        onImageSelected.onSelected(selectedPosition.size)
                    }
                }
            }
        }
    }

    fun addItems(items: List<Uri>) {
        items.forEach { imageList.add(it) }
        notifyDataSetChanged()
    }

    fun getSelectedPath(): ArrayList<String> =
        arrayListOf<String>().apply { selectedPosition.forEach { add(getPathFromUri(imageList[it]) ?: "") } }

    private fun getPathFromUri(uri: Uri): String? {

        val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            when {
                isDownloadsDocument(uri) -> {

                    val id = DocumentsContract.getDocumentId(uri)
                    val contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id)
                    )

                    return getDataColumn(context, contentUri, null, null)
                }
                isMediaDocument(uri) -> {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    val type = split[0]

                    var contentUri: Uri? = null
                    when (type) {
                        "image" -> contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        "video" -> contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                        "audio" -> contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    }

                    val selection = "_id=?"
                    val selectionArgs = arrayOf(split[1])

                    return getDataColumn(context, contentUri, selection, selectionArgs)
                }// MediaProvider
                // DownloadsProvider
            }// MediaProvider
            // DownloadsProvider
        } else if ("content".equals(uri.scheme!!, ignoreCase = true)) {

            // Return the remote address
            return if (isGooglePhotosUri(uri)) uri.lastPathSegment else getDataColumn(context, uri, null, null)

        } else if ("file".equals(uri.scheme!!, ignoreCase = true)) {
            return uri.path
        }// File
        // MediaStore (and general)

        return null
    }

    @SuppressLint("Recycle")
    fun getDataColumn(
        context: Context, uri: Uri?, selection: String?,
        selectionArgs: Array<String>?
    ): String? {

        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)

        try {
            cursor = uri?.let { context.contentResolver.query(it, projection, selection, selectionArgs, null) }
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }

    private fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    private fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    private fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.authority
    }
}