package com.pth.androidapp.presentation.main.fragments.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pth.androidapp.databinding.ItemImageSlideBinding
import com.pth.androidapp.databinding.ItemImageSlidePlaceholderBinding
import com.pth.androidapp.domain.models.ImageSlide

@SuppressLint("NotifyDataSetChanged")
class ImageSlideAdapter : ListAdapter<ImageSlide, RecyclerView.ViewHolder>(DiffCallback) {

    private var isLoading = true

    companion object {
        private const val VIEW_TYPE_IMAGE = 0
        private const val VIEW_TYPE_PLACEHOLDER = 1
        private const val PLACEHOLDER_COUNT = 3

        private val DiffCallback = object : DiffUtil.ItemCallback<ImageSlide>() {
            override fun areItemsTheSame(oldItem: ImageSlide, newItem: ImageSlide): Boolean =
                oldItem.imageUrl == newItem.imageUrl

            override fun areContentsTheSame(oldItem: ImageSlide, newItem: ImageSlide): Boolean =
                oldItem == newItem
        }
    }

    class ImageViewHolder(val binding: ItemImageSlideBinding) : RecyclerView.ViewHolder(binding.root)
    class PlaceholderViewHolder(val binding: ItemImageSlidePlaceholderBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemViewType(position: Int): Int {
        return if (isLoading) VIEW_TYPE_PLACEHOLDER else VIEW_TYPE_IMAGE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_IMAGE) {
            val binding = ItemImageSlideBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ImageViewHolder(binding)
        } else {
            val binding = ItemImageSlidePlaceholderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            PlaceholderViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ImageViewHolder) {
            if (currentList.isNotEmpty()) {
                val slide = getItem(position % currentList.size)
                Glide.with(holder.binding.root)
                    .load(slide.imageUrl)
                    .into(holder.binding.ivSlide)
            }
        }
    }

    override fun getItemCount(): Int {
        return when {
            isLoading -> PLACEHOLDER_COUNT
            currentList.isEmpty() -> 0
            else -> Int.MAX_VALUE
        }
    }

    fun setData(newSlides: List<ImageSlide>) {
        isLoading = false
        submitList(newSlides)
    }

    fun showLoading() {
        isLoading = true
        notifyDataSetChanged()
    }
}
