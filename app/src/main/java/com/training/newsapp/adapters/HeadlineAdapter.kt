package com.training.newsapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.training.newsapp.R
import com.training.newsapp.databinding.HeadlineItemBinding
import com.training.newsapp.dataclasses.Headline

class HeadlineAdapter: RecyclerView.Adapter<HeadlineAdapter.HeadlineHolder>() {

    val headlineList = ArrayList<Headline>()

    inner class HeadlineHolder(item: View): RecyclerView.ViewHolder(item) {

        val binding = HeadlineItemBinding.bind(item)

        fun bind(headline: Headline) = with(binding) {
            tvHeader.text = headline.title
            tvSource.text = headline.source.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeadlineHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.headline_item, parent, false)
        return HeadlineHolder(view)
    }

    override fun getItemCount(): Int {
        return headlineList.size
    }

    override fun onBindViewHolder(holder: HeadlineHolder, position: Int) {
        holder.bind(headlineList[position])
    }


    fun addHeadline(headline: Headline){
        headlineList.add(headline)
        notifyDataSetChanged()
    }
}