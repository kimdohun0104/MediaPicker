package com.dsm.mediapicker.ui

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
import kotlinx.android.synthetic.main.item_image.view.*

class ImageListAdapter(
    private val context: Context,
    private val maxCount: Int,
    private val onImageSelected: (count: Int) -> Unit
) : RecyclerView.Adapter<ImageListAdapter.ImageViewHolder>() {

    var imageList = listOf<Uri>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

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
                    onImageSelected(selectedPosition.size)
                } else {
                    // single mode
                    if (maxCount == 1) {
                        originalSelectedView?.ll_check?.visibility = View.INVISIBLE
                        originalSelectedView = it
                        selectedPosition.clear()
                        selectedPosition.add(adapterPosition)
                        itemView.ll_check.visibility = View.VISIBLE
                        onImageSelected(selectedPosition.size)
                    } else {
                        if (selectedPosition.size == maxCount)
                            return@setOnClickListener
                        selectedPosition.add(adapterPosition)
                        itemView.ll_check.visibility = View.VISIBLE
                        onImageSelected(selectedPosition.size)
                    }
                }
            }
        }
    }

    fun getSelectedPath(): ArrayList<String> =
        selectedPosition.map { getPathFromUri(imageList[it]) ?: "" } as ArrayList<String>

    private fun getPathFromUri(uri: Uri): String? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, uri)) {
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
                    val contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

                    val selection = "_id=?"
                    val selectionArgs = arrayOf(split[1])

                    return getDataColumn(context, contentUri, selection, selectionArgs)
                }
            }
        } else if ("content".equals(uri.scheme!!, ignoreCase = true)) {
            return if (isGooglePhotosUri(uri)) uri.lastPathSegment else getDataColumn(context, uri, null, null)
        } else if ("file".equals(uri.scheme!!, ignoreCase = true)) {
            return uri.path
        }

        return null
    }

    private fun getDataColumn(
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

    private fun isDownloadsDocument(uri: Uri): Boolean = "com.android.providers.downloads.documents" == uri.authority

    private fun isMediaDocument(uri: Uri): Boolean = "com.android.providers.media.documents" == uri.authority

    private fun isGooglePhotosUri(uri: Uri): Boolean = "com.google.android.apps.photos.content" == uri.authority
}