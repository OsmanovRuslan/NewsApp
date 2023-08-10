package com.training.newsapp.fragments.headline

import android.annotation.SuppressLint
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import com.training.newsapp.databinding.FragmentHeadlineBinding
import com.training.newsapp.fragments.ViewBindingFragment
import com.training.newsapp.retrofit.dataclasses.Headline


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
        val headline: Headline? = arguments?.parcelable("headline")
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
            }
        }

    }


    private inline fun <reified T : Parcelable> Bundle.parcelable(key: String): T? = when {
        SDK_INT >= 33 -> getParcelable(key, T::class.java)
        else -> @Suppress("DEPRECATION") getParcelable(key) as? T
    }
}