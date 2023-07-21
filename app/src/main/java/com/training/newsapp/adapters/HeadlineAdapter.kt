package com.training.newsapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.training.newsapp.R
import com.training.newsapp.databinding.HeadlineItemBinding
import com.training.newsapp.dataclasses.Headline

class HeadlineAdapter(
    private val onItemClick: (Headline) -> Unit
): PagingDataAdapter<Headline, HeadlineAdapter.HeadlineHolder>(HeadlineDiffCallback) {

    inner class HeadlineHolder(private val binding: HeadlineItemBinding): RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val headline = getItem(position)
                    if (headline != null) {
                        onItemClick(headline)
                    }
                }
            }
        }
        fun bind(headline: Headline) = with(binding) {
            tvHeader.text = headline.title
            if (headline.source.name == "Google News"){
                tvSource.text = headline.author ?: headline.source.name
            } else{
                tvSourceName.text = headline.source.name ?:headline.author ?: "News"
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeadlineHolder {
        val binding = HeadlineItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HeadlineHolder(binding)
    }


    override fun onBindViewHolder(holder: HeadlineHolder, position: Int) {
        holder.bind(getItem(position) ?: return)
    }

    object HeadlineDiffCallback : DiffUtil.ItemCallback<Headline>() {
        override fun areItemsTheSame(oldItem: Headline, newItem: Headline): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: Headline, newItem: Headline): Boolean {
            return oldItem.content == newItem.content && oldItem.author == newItem.author
        }
    }
}