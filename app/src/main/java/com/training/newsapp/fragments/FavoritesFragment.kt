package com.training.newsapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.training.newsapp.R
import com.training.newsapp.adapters.FavoritesAdapter
import com.training.newsapp.adapters.HeadlineAdapter
import com.training.newsapp.database.Headlines
import com.training.newsapp.database.MainDb
import com.training.newsapp.databinding.FragmentFavoritesBinding
import com.training.newsapp.dataclasses.Headline
import com.training.newsapp.dataclasses.Source
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch


class FavoritesFragment : ViewBindingFragment<FragmentFavoritesBinding>() {

    private lateinit var db: MainDb
    private lateinit var favoritesAdapter: FavoritesAdapter
    private val allHeadlinesFlow: Flow<List<Headlines>> by lazy {
        db.getDao().getHeadlines().flowOn(Dispatchers.IO)
    }

    override fun makeBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): FragmentFavoritesBinding {
        return FragmentFavoritesBinding.inflate(inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = MainDb.getDb(requireContext())

        favoritesAdapter = FavoritesAdapter(
            requireContext(),
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
        }, onButtonClickListener = { headline, method ->
            if (method == "add") {
                lifecycleScope.launch(Dispatchers.IO) {
                    addToDatabase(
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
                }
            } else {
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
                }
            }
        })

        binding.rvFavorites.layoutManager = LinearLayoutManager(this@FavoritesFragment.context)
        binding.rvFavorites.adapter = favoritesAdapter

        lifecycleScope.launch {
            allHeadlinesFlow.collectLatest {
                favoritesAdapter.submitData(it)
            }
        }
    }

    private fun addToDatabase(headline: Headline) {
        db.getDao().insertHeadline(
            Headlines(
                headline.author,
                headline.content,
                headline.description,
                headline.publishedAt,
                headline.source?.id,
                headline.source?.name,
                headline.title,
                headline.url,
                headline.urlToImage
            )
        )
    }

    private fun deleteFromDatabase(headline: Headline) {
        println(headline.title + headline.source?.name + headline.publishedAt)
        db.getDao().deleteHeadline(headline.title)
    }

    private fun loadFragment(headline: Headline) {
        val bundle = Bundle()
        bundle.putParcelable("headline", headline)
        findNavController().navigate(
            R.id.action_favoritesFragment_to_headlineFragment, bundle
        )
    }

}