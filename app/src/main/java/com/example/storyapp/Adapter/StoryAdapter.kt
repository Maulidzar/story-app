package com.example.storyapp.Adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyapp.Data.StoryData.StoryUnit
import com.example.storyapp.databinding.ItemStoryBinding

class   StoryAdapter(private val onClick: (StoryUnit, ActivityOptionsCompat) -> Unit) :
    PagingDataAdapter<StoryUnit, StoryAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)

        if (data != null) {
            holder.binding.tvItemName.text = data.name
            Glide.with(holder.itemView)
                .load(data.photoUrl)
                .into(holder.binding.ivItemPhoto)
            holder.binding.tvItemDesc.text = data.desc

            holder.itemView.setOnClickListener {
                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        holder.itemView.context as Activity,
                        Pair(holder.binding.ivItemPhoto, "photo"),
                        Pair(holder.binding.tvItemName, "name"),
                        Pair(holder.binding.tvItemDesc, "description")
                    )
                onClick(data, optionsCompat)
            }
        }
    }

    class ViewHolder(val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryUnit>() {
            override fun areItemsTheSame(oldItem: StoryUnit, newItem: StoryUnit): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: StoryUnit, newItem: StoryUnit): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}