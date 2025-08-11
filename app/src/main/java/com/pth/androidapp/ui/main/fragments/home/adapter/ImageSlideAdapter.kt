package com.pth.androidapp.ui.main.fragments.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.pth.androidapp.R
import com.pth.androidapp.data.models.imageSlide.ImageSlide

class ImageSlideAdapter(
    private var slides: List<ImageSlide>,
    private val viewPager2: ViewPager2
) : RecyclerView.Adapter<ImageSlideAdapter.ImageViewHolder>() {

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val slideImage: ImageView = itemView.findViewById(R.id.slide_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_image_slide, parent, false)
        return ImageViewHolder(view)
    }

    override fun getItemCount(): Int = slides.size

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val slide = slides[position]
        Glide.with(holder.itemView)
            .load(slide.imageUrl)
            .into(holder.slideImage)

        if (position == slides.size - 1) {
            viewPager2.post { notifyDataSetChanged() }
        }
    }

    /**
     * Updates the adapter's data and refreshes the ViewPager.
     */
    fun setData(newSlides: List<ImageSlide>) {
        slides = newSlides
        notifyDataSetChanged()
    }
}