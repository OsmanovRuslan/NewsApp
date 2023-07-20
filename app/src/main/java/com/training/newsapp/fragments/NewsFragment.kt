package com.training.newsapp.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.training.newsapp.R
import com.training.newsapp.adapters.HeadlineAdapter
import com.training.newsapp.databinding.FragmentNewsBinding
import com.training.newsapp.dataclasses.Headline
import com.training.newsapp.paging.NewsPagingSource
import com.training.newsapp.retrofit.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NewsFragment : ViewBindingFragment<FragmentNewsBinding>() {

    private val retrofitParse = RetrofitInstance
    private lateinit var headlineAdapter: HeadlineAdapter

    @SuppressLint("DiscouragedApi", "InternalInsetResource")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        headlineAdapter = HeadlineAdapter { headline ->
            loadFragment(headline)
        }
        binding.rvNews.layoutManager = LinearLayoutManager(this@NewsFragment.context)
        binding.rvNews.adapter = headlineAdapter
        loadData()
    }

    override fun makeBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentNewsBinding{
        return FragmentNewsBinding.inflate(inflater)
    }

    private fun loadData() {
        val moviesFlow: Flow<PagingData<Headline>> = Pager(config = PagingConfig(pageSize = 20)) {
            NewsPagingSource(retrofitParse)
        }.flow

        CoroutineScope(Dispatchers.Main).launch {
            moviesFlow.collectLatest { pagingData ->
                headlineAdapter.submitData(pagingData)
            }
        }
    }

    private fun loadFragment(headline: Headline) {
        val bundle = Bundle()
        bundle.putParcelable("headline", headline)
        findNavController().navigate(
            R.id.action_newsFragment_to_headlineFragment,
            bundle,
            navOptions {
                popUpTo(R.id.newsFragment)
            }
        )
    }
}