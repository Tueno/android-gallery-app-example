package com.example.gallery.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Build
import android.provider.MediaStore
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.*

interface GalleryViewModelInterface {
    val listItems: LiveData<List<GalleryItemViewData>>
    fun fetchGalleryItemsIfNeeded(resolver: ContentResolver)
    fun requiredPermissions() : Array<String>
    fun hasRequiredPermissions(context: Context?) : Boolean
}

class GalleryViewModel : ViewModel(), GalleryViewModelInterface {
    override val listItems: MutableLiveData<List<GalleryItemViewData>> = MutableLiveData(listOf())

    /**
     *  DIを使わない場合、ContentResolverを引数として渡す必要がある。
     */
    override fun fetchGalleryItemsIfNeeded(resolver: ContentResolver) {
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATE_TAKEN,
            MediaStore.Images.Media.MIME_TYPE
        )
//        val selection = "${MediaStore.Images.Media.DATE_TAKEN} >= ?"
//        val selectionArgs = arrayOf(
//            dateToTimestamp(day = 1, month = 1, year = 2020).toString()
//        )
        val sortOrder = "${MediaStore.Images.Media.DATE_TAKEN} DESC"
        resolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            sortOrder
        )?.use {
            listItems.value = addImagesFromCursor(it)
        }
    }

    override fun hasRequiredPermissions(context: Context?) : Boolean {
        context?.also {
            requiredPermissions().forEach {
                if (ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED) {
                    return false
                }
            }
        } ?: run {
            return false
        }
        return true
    }

    override fun requiredPermissions() : Array<String> {
        return arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    @Suppress("SameParameterValue")
    @SuppressLint("SimpleDateFormat")
    private fun dateToTimestamp(day: Int, month: Int, year: Int): Long =
        SimpleDateFormat("dd.MM.yyyy").let { formatter ->
            formatter.parse("$day.$month.$year")?.time ?: 0
        }

    @TargetApi(Build.VERSION_CODES.Q)
    private fun addImagesFromCursor(cursor: Cursor): List<GalleryItemViewData> {
        val images = mutableListOf<GalleryItemViewData>()


        val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
        val dateTakenColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN)
        val displayNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
        val mimeTypeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE)

        while (cursor.moveToNext()) {

            val id = cursor.getLong(idColumn)
            val dateTaken = Date(cursor.getLong(dateTakenColumn))
            val displayName = cursor.getString(displayNameColumn)

            val contentUri = ContentUris.withAppendedId(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                id
            )

            val mimeType = cursor.getString(mimeTypeColumn)

            val image = GalleryItemViewData(id, displayName, dateTaken, contentUri, mimeType)
            images += image

        }
        return images
    }
}
