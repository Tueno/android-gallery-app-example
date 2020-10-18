package com.example.gallery.presentation

import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("imageURL")
fun ImageView.imageURI(uri: Uri?) {
    uri?.let {
        Glide.with(this)
            .load(it)
            .into(this)
    }
}
