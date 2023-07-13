package com.training.newsapp.retrofit

import com.training.newsapp.dataclasses.News
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    @GET("/v2/top-headlines?country=ru")
    fun getTopNewsRu(@Query("apiKey") apiKey: String): Response<News>
}