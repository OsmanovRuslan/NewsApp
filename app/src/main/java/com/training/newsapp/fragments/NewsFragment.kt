package com.training.newsapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.training.newsapp.R
import com.training.newsapp.adapters.HeadlineAdapter
import com.training.newsapp.databinding.FragmentNewsBinding
import com.training.newsapp.dataclasses.Headline
import com.training.newsapp.dataclasses.Source

class NewsFragment : ViewBindingFragment<FragmentNewsBinding>() {

    private val adapter = HeadlineAdapter()
    private val sourceFirst = Source("12", "Google News")
    private val first = Headline("isngoids","smfdomospfdm",
        "dsgfdsf", "14124 14314",sourceFirst,
        "Can banks push Bitcoin to clean up its act? ",
        "fghfg", "sdfdsgsdgf")


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    override fun makeBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentNewsBinding{
        return FragmentNewsBinding.inflate(inflater)
    }

    private fun init(){
        binding.apply {
            rvNews.layoutManager = LinearLayoutManager(this@NewsFragment.context)
            rvNews.adapter = adapter
            adapter.addHeadline(first)
            adapter.addHeadline(first)
        }
    }
}