package com.pth.androidapp.ui.auth.fragments.jsonPlaceHolder

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pth.androidapp.data.models.post.Post
import com.pth.androidapp.databinding.ViewholderJsonPlaceHolderBinding

class JsonPlaceHolderAdapter(
    private var items: List<Post>
) : RecyclerView.Adapter<JsonPlaceHolderAdapter.ViewHolder>() {

    inner class ViewHolder(binding: ViewholderJsonPlaceHolderBinding) : RecyclerView.ViewHolder(binding.root) {
        val titleView: TextView = binding.title
        val contentView: TextView = binding.content
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            ViewholderJsonPlaceHolderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.titleView.text = item.title
        holder.contentView.text = item.body
    }

    override fun getItemCount(): Int = items.size

    fun updateData(newItems: List<Post>) {
        items = newItems
        notifyDataSetChanged()
    }
}