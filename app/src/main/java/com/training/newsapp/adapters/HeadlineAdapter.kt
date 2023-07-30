package com.training.newsapp.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.training.newsapp.R
import com.training.newsapp.database.Headlines
import com.training.newsapp.databinding.HeadlineItemBinding
import com.training.newsapp.dataclasses.Headline
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HeadlineAdapter(
    context: Context,
    private val onItemClick: (Headline) -> Unit,
    private val onButtonClickListener: (Headline, String) -> Unit
) : PagingDataAdapter<Headline, HeadlineAdapter.HeadlineHolder>(HeadlineDiffCallback) {

    private var headlines: List<Headlines> = emptyList()
    @SuppressLint("NotifyDataSetChanged")
    inner class HeadlineHolder(private val binding: HeadlineItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
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

            binding.btnFavorite.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val headline = getItem(position)
                    if (headline != null) {
                        if (binding.btnFavorite.background == ContextCompat.getDrawable(
                                binding.root.context,
                                R.drawable.favorite_pressed_icon
                            )
                        ) {
                            println("DELETEFROMDATABASE")
                            onButtonClickListener(headline, "delete")
                            binding.btnFavorite.setBackgroundResource(R.drawable.favorite_icon)
                        } else {
                            println("ADDTODATABASE")
                            onButtonClickListener(headline, "add")
                            binding.btnFavorite.setBackgroundResource(R.drawable.favorite_pressed_icon)
                        }
                    }
                }
            }

        }

        fun bind(headline: Headline) = with(binding) {
            tvHeader.text = headline.title
            if (headline.source?.name == "Google News") {
                tvSource.text = headline.author ?: headline.source.name
            } else {
                tvSourceName.text = headline.source?.name ?: headline.author ?: "News"
            }
            if (isInside(headline)){
                println("INSIDE")
                println(headline.title)
                binding.btnFavorite.setBackgroundResource(R.drawable.favorite_pressed_icon)
            } else{
                println("NOTINSIDE")
                println(headline.title)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeadlineHolder {
        val binding =
            HeadlineItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HeadlineHolder(binding)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitData(headlines: List<Headlines>) {
        this.headlines = headlines
        notifyDataSetChanged()
    }
    fun isInside(headline: Headline): Boolean{
        var here = false
        for (item in headlines){
            if (item.title == headline.title){
                here = true
                break
            }
        }
        return here
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