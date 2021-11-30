package com.codinginflow.imagesearchapp

import androidx.recyclerview.widget.DiffUtil
import com.codinginflow.imagesearchapp.data.UnsplashPhoto

class PhotoComparator {
    companion object {
        val PHOTO_COMPARATOR = object : DiffUtil.ItemCallback<UnsplashPhoto>() {
            override fun areItemsTheSame(oldItem: UnsplashPhoto, newItem: UnsplashPhoto) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: UnsplashPhoto, newItem: UnsplashPhoto) =
                oldItem == newItem
        }
    }
}