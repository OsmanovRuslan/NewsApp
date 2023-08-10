package com.training.newsapp.fragments.news

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.training.newsapp.R
import com.training.newsapp.adapters.HeadlineAdapter
import com.training.newsapp.database.Headlines
import com.training.newsapp.databinding.FragmentNewsBinding
import com.training.newsapp.fragments.ViewBindingFragment
import com.training.newsapp.retrofit.dataclasses.Headline
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class NewsFragment : ViewBindingFragment<FragmentNewsBinding>() {

    private lateinit var headlineAdapter: HeadlineAdapter


    private val vm by lazy {
        ViewModelProvider(this)[NewsViewModel::class.java]
    }

    @SuppressLint("DiscouragedApi", "InternalInsetResource")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm.connectToDb(requireContext())

       headlineAdapter = HeadlineAdapter(
            onItemClick = { headline ->
                loadFragment(headline)
            },
            onButtonClickListener = { headline, method ->
                if (method == "add") {
                    lifecycleScope.launch(Dispatchers.IO) {
                        addToDatabase(headline)
                    }
                } else {
                    lifecycleScope.launch(Dispatchers.IO) {
                        deleteFromDatabase(headline)
                    }
                }
            }
        )

        binding.rvNews.layoutManager = LinearLayoutManager(this@NewsFragment.context)
        binding.rvNews.adapter = headlineAdapter

        binding.svNews.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(text: String) = false

            override fun onQueryTextChange(newText: String): Boolean {
                vm.searchHeadline(newText)
                headlineAdapter.refresh()
                return true
            }
        })

        lifecycleScope.launch {
            vm.allHeadlinesFlow
                .collectLatest { headlineAdapter.submitDataFlow(it) }
        }

        lifecycleScope.launch {
            vm.newsFlow
                .collectLatest { headlineAdapter.submitData(it) }
        }

//        vm.newsFlow
//            .onEach { headlineAdapter.submitData(it) }
//            .launchIn(lifecycleScope)
//
//
//        vm.allHeadlinesFlow
//            .onEach { headlineAdapter.submitDataFlow(it) }
//            .launchIn(lifecycleScope)

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
        vm.db.getDao().insertHeadline(
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
        vm.db.getDao().deleteHeadline(headline.title)
    }
}