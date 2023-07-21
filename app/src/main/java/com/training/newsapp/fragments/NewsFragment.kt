package com.training.newsapp.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.training.newsapp.R
import com.training.newsapp.adapters.HeadlineAdapter
import com.training.newsapp.databinding.FragmentNewsBinding
import com.training.newsapp.dataclasses.Headline
import com.training.newsapp.paging.NewsPagingSource
import com.training.newsapp.paging.SearchNewsPagingSource
import com.training.newsapp.retrofit.RetrofitInstance
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NewsFragment : ViewBindingFragment<FragmentNewsBinding>() {

    private val retrofitInstance = RetrofitInstance
    private lateinit var headlineAdapter: HeadlineAdapter
    private lateinit var newsFlow: Flow<PagingData<Headline>>
    private var query = ""
    @SuppressLint("DiscouragedApi", "InternalInsetResource")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        headlineAdapter = HeadlineAdapter { headline ->
            loadFragment(headline)
        }

        newsFlow = Pager(config = PagingConfig(pageSize = 20)){
            if (query.isNotBlank()){
                SearchNewsPagingSource(retrofitInstance, query)
            }else{
                NewsPagingSource(retrofitInstance)
            }
        }.flow


        binding.rvNews.layoutManager = LinearLayoutManager(this@NewsFragment.context)
        binding.rvNews.adapter = headlineAdapter

        binding.svNews.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(text: String) = false

            override fun onQueryTextChange(newText: String): Boolean {
                query = newText
                headlineAdapter.refresh()
                return true
            }
        })

        lifecycleScope.launch {
            newsFlow.collectLatest { pagingData ->
                headlineAdapter.submitData(pagingData)
            }
        }
    }

    override fun makeBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentNewsBinding{
        return FragmentNewsBinding.inflate(inflater)
    }


    private fun loadFragment(headline: Headline) {
        val bundle = Bundle()
        bundle.putParcelable("headline", headline)
        findNavController().navigate(
            R.id.action_newsFragment_to_headlineFragment,
            bundle
        )
    }
}