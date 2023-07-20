package com.training.newsapp.retrofit

import com.training.newsapp.dataclasses.News
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    @GET("top-headlines?country=ru&pageSize=20")
    suspend fun getTopNewsRu(
        @Query("apiKey") apiKey: String,
        @Query("page") page: Int
    ): Response<News>

}