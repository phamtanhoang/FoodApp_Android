package com.pth.androidapp.ui.main.fragments.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.pth.androidapp.data.models.imageSlide.ImageSlide
import com.pth.androidapp.databinding.ItemImageSlideBinding

class ImageSlideAdapter : ListAdapter<ImageSlide, ImageSlideAdapter.ImageViewHolder>(DiffCallback) {

    inner class ImageViewHolder(val binding: ItemImageSlideBinding) : androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ItemImageSlideBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val slide = getItem(position % currentList.size)
        Glide.with(holder.binding.root)
            .load(slide.imageUrl)
            .into(holder.binding.slideImage)
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<ImageSlide>() {
            override fun areItemsTheSame(oldItem: ImageSlide, newItem: ImageSlide): Boolean = oldItem.imageUrl == newItem.imageUrl
            override fun areContentsTheSame(oldItem: ImageSlide, newItem: ImageSlide): Boolean = oldItem == newItem
        }
    }

    override fun getItemCount(): Int = if (currentList.isEmpty()) 0 else Int.MAX_VALUE

    fun setData(newSlides: List<ImageSlide>) {
        submitList(newSlides)
    }
}