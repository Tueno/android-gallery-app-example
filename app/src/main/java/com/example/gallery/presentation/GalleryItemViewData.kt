package com.example.gallery.presentation

import android.net.Uri
import java.util.*

data class GalleryItemViewData(val id: Long,
                               val displayName: String,
                               val dateTaken: Date,
                               val contentUri: Uri,
                               val mimeType: String)