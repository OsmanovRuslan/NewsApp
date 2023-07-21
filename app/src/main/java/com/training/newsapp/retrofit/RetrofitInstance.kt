package com.training.newsapp.retrofit

import com.training.newsapp.dataclasses.Headline
import com.training.newsapp.dataclasses.News
import com.training.newsapp.dataclasses.Source
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object RetrofitInstance {
    val api: NewsApi by lazy {
        Retrofit.Builder()
            .baseUrl(RetrofitUrls.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsApi::class.java)
    }
}