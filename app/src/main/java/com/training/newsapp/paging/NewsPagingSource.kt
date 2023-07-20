package com.training.newsapp.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.training.newsapp.dataclasses.Headline
import com.training.newsapp.retrofit.RetrofitInstance
import com.training.newsapp.retrofit.RetrofitUrls

class NewsPagingSource(private val retrofitParse: RetrofitInstance) : PagingSource<Int, Headline>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Headline> {
        return try {
            val page = params.key ?: 1
            val response = retrofitParse.api.getTopNewsRu(RetrofitUrls.API_KEY, page)
//            val totalPage = response.body()!!.totalResults / 100

            LoadResult.Page(
                data = response.body()!!.articles,
                prevKey = if (page > 1) page - 1 else null,
                nextKey = if (response.body()!!.articles.isNotEmpty()) page + 1 else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Headline>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}