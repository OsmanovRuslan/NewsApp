package com.training.newsapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.training.newsapp.R
import com.training.newsapp.databinding.FragmentSettingsBinding

class SettingsFragment : ViewBindingFragment<FragmentSettingsBinding>() {

    override fun makeBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSettingsBinding {
        return FragmentSettingsBinding.inflate(inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }


}