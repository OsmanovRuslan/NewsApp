package com.training.newsapp.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.training.newsapp.R
import com.training.newsapp.database.Headlines
import com.training.newsapp.database.MainDb
import com.training.newsapp.databinding.HeadlineItemBinding

class FavoritesAdapter(
    context: Context,
    private val onItemClick: (Headlines) -> Unit,
    private val onButtonClickListener: (Headlines) -> Unit,
) : RecyclerView.Adapter<FavoritesAdapter.HeadlineViewHolder>() {

    private var headlines: List<Headlines> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeadlineViewHolder {
        val binding =
            HeadlineItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HeadlineViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HeadlineViewHolder, position: Int) {
        holder.bind(headlines[position])
    }

    override fun getItemCount(): Int {
        return headlines.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitDataFlow(headlines: List<Headlines>) {
        this.headlines = headlines
        notifyDataSetChanged()
    }

    inner class HeadlineViewHolder(private val binding: HeadlineItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val headline = headlines[position]
                    onItemClick(headline)
                }
            }
            binding.btnFavorite.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val headline = headlines[position]
                    onButtonClickListener(headline)
                }
            }
        }

        fun bind(headlines: Headlines) = with(binding) {
            tvHeader.text = headlines.title
            if (headlines.sourceName == "Google News") {
                tvSource.text = headlines.author ?: headlines.sourceName
            } else {
                tvSourceName.text = headlines.sourceName ?: headlines.author ?: "News"
            }
            binding.btnFavorite.setBackgroundResource(R.drawable.favorite_pressed_icon)
        }
    }
}