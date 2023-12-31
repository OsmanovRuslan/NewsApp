package com.training.newsapp.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.training.newsapp.R
import com.training.newsapp.adapters.HeadlineAdapter
import com.training.newsapp.database.Headlines
import com.training.newsapp.database.MainDb
import com.training.newsapp.databinding.FragmentNewsBinding
import com.training.newsapp.dataclasses.Headline
import com.training.newsapp.paging.NewsPagingSource
import com.training.newsapp.paging.SearchNewsPagingSource
import com.training.newsapp.retrofit.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class NewsFragment : ViewBindingFragment<FragmentNewsBinding>() {

    private val retrofitInstance = RetrofitInstance
    private lateinit var headlineAdapter: HeadlineAdapter
    private lateinit var newsFlow: Flow<PagingData<Headline>>
    private var query = ""
    private lateinit var db: MainDb

    private val allHeadlinesFlow: Flow<List<Headlines>> by lazy {
        db.getDao().getHeadlines().flowOn(Dispatchers.IO)
    }

    @SuppressLint("DiscouragedApi", "InternalInsetResource")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = MainDb.getDb(requireContext())

        headlineAdapter = HeadlineAdapter(
            requireContext(),
            onItemClick = { headline ->
                loadFragment(headline)
            },
            onButtonClickListener = { headline, method ->
                if (method == "add") {
                    lifecycleScope.launch(Dispatchers.IO) {
                        addToDatabase(headline)
                        launch(Dispatchers.Main) {
                            allHeadlinesFlow.collectLatest {
                                headlineAdapter.submitDataFlow(it)
                            }
                        }
                    }
                } else {
                    lifecycleScope.launch(Dispatchers.IO) {
                        deleteFromDatabase(headline)
                        launch(Dispatchers.Main) {
                            allHeadlinesFlow.collectLatest {
                                headlineAdapter.submitDataFlow(it)
                            }
                        }
                    }
                }
            }
        )

        newsFlow = Pager(config = PagingConfig(pageSize = 20)) {
            if (query.isNotBlank()) {
                SearchNewsPagingSource(retrofitInstance, query)
            } else {
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

        lifecycleScope.launch {
            allHeadlinesFlow.collectLatest {
                headlineAdapter.submitDataFlow(it)
            }
        }
    }

    override fun makeBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentNewsBinding {
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

    private fun addToDatabase(headline: Headline) {
        db.getDao().insertHeadline(
            Headlines(
                headline.author,
                headline.content, headline.description,
                headline.publishedAt, headline.source?.id,
                headline.source?.name, headline.title,
                headline.url, headline.urlToImage
            )
        )
    }

    private fun deleteFromDatabase(headline: Headline) {
        db.getDao().deleteHeadline(headline.title)
    }
}