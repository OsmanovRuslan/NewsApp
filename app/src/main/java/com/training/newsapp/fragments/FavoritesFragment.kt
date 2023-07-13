package com.training.newsapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.training.newsapp.R
import com.training.newsapp.databinding.FragmentFavoritesBinding


class FavoritesFragment : ViewBindingFragment<FragmentFavoritesBinding>() {

    override fun makeBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFavoritesBinding {
        return FragmentFavoritesBinding.inflate(inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }


}