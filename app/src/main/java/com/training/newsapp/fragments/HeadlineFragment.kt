package com.training.newsapp.fragments

import android.annotation.SuppressLint
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navOptions
import com.training.newsapp.R
import com.training.newsapp.databinding.FragmentHeadlineBinding
import com.training.newsapp.dataclasses.Headline


class HeadlineFragment : ViewBindingFragment<FragmentHeadlineBinding>() {

    override fun makeBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHeadlineBinding {
        return FragmentHeadlineBinding.inflate(inflater)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val headline : Headline?= arguments?.parcelable("headline")
        if (headline != null) {
            print(headline.url)
            print(headline.title)
        }
        binding.apply {
            wvHeadline.webViewClient = WebViewClient()
            if (headline != null) {
                headline.url?.let { wvHeadline.loadUrl(it) }
                wvHeadline.settings.javaScriptEnabled = true
                wvHeadline.settings.setSupportZoom(true)
            } else{
                loadFragment()
            }
        }

    }


    private inline fun <reified T : Parcelable> Bundle.parcelable(key: String): T? = when {
        SDK_INT >= 33 -> getParcelable(key, T::class.java)
        else -> @Suppress("DEPRECATION") getParcelable(key) as? T
    }

    private fun loadFragment() {
        findNavController().navigate(
            R.id.action_headlineFragment_to_newsFragment,
            null,
            navOptions {
                popUpTo(R.id.headlineFragment)
            }
        )
    }
}