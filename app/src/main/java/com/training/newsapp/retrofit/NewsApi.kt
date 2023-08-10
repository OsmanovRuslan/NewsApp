package com.training.newsapp.retrofit

import com.training.newsapp.retrofit.dataclasses.News
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("top-headlines?country=us&pageSize=20")
    suspend fun getTopNewsUs(
        @Query("apiKey") apiKey: String,
        @Query("page") page: Int
    ): News

    @GET("everything?language=en&pageSize=20")
    suspend fun getSearchNewsUs(
        @Query("apiKey") apiKey: String,
        @Query("q") query: String,
        @Query("page") page: Int
    ): News

}