package com.example.gallery.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.gallery.R
import com.example.gallery.databinding.GalleryItemViewBinding

interface ItemsSettableInterface {
    fun setItems(items: List<Any>)
}

class GalleryItemAdapter : RecyclerView.Adapter<GalleryItemAdapter.GalleryItemVH>(), ItemsSettableInterface {

    private var galleryItems: List<GalleryItemViewData> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryItemVH {
        return GalleryItemVH(parent)
    }

    override fun onBindViewHolder(holder: GalleryItemVH, position: Int) {
        val item = galleryItems[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return galleryItems.count()
    }

    inner class GalleryItemVH(
        private val parent: ViewGroup,
        private val binding: GalleryItemViewBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.gallery_item_view,
            parent,
            false
        )
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(viewData: GalleryItemViewData) {
            binding.viewData = viewData
        }
    }

    override fun setItems(items: List<Any>) {
        galleryItems = items as List<GalleryItemViewData>
    }

}