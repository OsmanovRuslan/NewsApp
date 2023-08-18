package com.training.newsapp.fragments.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.training.newsapp.R
import com.training.newsapp.adapters.FavoritesAdapter
import com.training.newsapp.database.Headlines
import com.training.newsapp.database.MainDb
import com.training.newsapp.databinding.FragmentFavoritesBinding
import com.training.newsapp.fragments.ViewBindingFragment
import com.training.newsapp.fragments.news.NewsViewModel
import com.training.newsapp.retrofit.dataclasses.Headline
import com.training.newsapp.retrofit.dataclasses.Source
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class FavoritesFragment : ViewBindingFragment<FragmentFavoritesBinding>() {

    private lateinit var favoritesAdapter: FavoritesAdapter
    private val vm: FavoritesViewModel by viewModel()
    override fun makeBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): FragmentFavoritesBinding {
        return FragmentFavoritesBinding.inflate(inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm.connectToDb(requireContext())


        favoritesAdapter = FavoritesAdapter(
            onItemClick = { headline ->
            loadFragment(
                Headline(
                    headline.author,
                    headline.content,
                    headline.description,
                    headline.publishedAt,
                    Source(headline.sourceId, headline.sourceName),
                    headline.title,
                    headline.url,
                    headline.urlToImage
                )
            )
        }, onButtonClickListener = { headline ->
                lifecycleScope.launch(Dispatchers.IO) {
                    deleteFromDatabase(
                        Headline(
                            headline.author,
                            headline.content,
                            headline.description,
                            headline.publishedAt,
                            Source(headline.sourceId, headline.sourceName),
                            headline.title,
                            headline.url,
                            headline.urlToImage
                        )
                    )
                    launch(Dispatchers.Main) {
                        vm.allHeadlinesFlow
                            .collectLatest { favoritesAdapter.submitDataFlow(it) }
                    }
                }
        })

        binding.rvFavorites.layoutManager = LinearLayoutManager(this@FavoritesFragment.context)
        binding.rvFavorites.adapter = favoritesAdapter

        lifecycleScope.launch {
            vm.allHeadlinesFlow
                .collectLatest { favoritesAdapter.submitDataFlow(it) }
        }
//        vm.allHeadlinesFlow
//            .onEach { favoritesAdapter.submitDataFlow(it) }
//            .launchIn(lifecycleScope)
    }


    private fun deleteFromDatabase(headline: Headline) {
        vm.db.getDao().deleteHeadline(headline.title)
    }

    private fun loadFragment(headline: Headline) {
        val bundle = Bundle()
        bundle.putParcelable("headline", headline)
        findNavController().navigate(
            R.id.action_favoritesFragment_to_headlineFragment, bundle
        )
    }

}