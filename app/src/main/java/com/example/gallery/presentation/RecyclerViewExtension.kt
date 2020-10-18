package com.example.gallery.presentation

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView

@BindingAdapter("android:items")
fun RecyclerView.setItems(items: List<Any>?) {
    val galleryItemAdapter = adapter as ItemsSettableInterface
    galleryItemAdapter.setItems(items ?: listOf())
}
