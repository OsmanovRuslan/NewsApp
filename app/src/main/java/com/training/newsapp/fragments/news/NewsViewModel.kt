package com.training.newsapp.fragments.news

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.training.newsapp.database.Headlines
import com.training.newsapp.database.MainDb
import com.training.newsapp.paging.NewsPagingSource
import com.training.newsapp.paging.SearchNewsPagingSource
import com.training.newsapp.retrofit.RetrofitInstance
import com.training.newsapp.retrofit.dataclasses.Headline
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class NewsViewModel : ViewModel() {

    private val retrofitInstance = RetrofitInstance
    private var query = ""
    lateinit var db: MainDb

    val allHeadlinesFlow: Flow<List<Headlines>> by lazy {
        db.getDao().getHeadlines().flowOn(Dispatchers.IO)
    }

    var newsFlow: Flow<PagingData<Headline>> = Pager(config = PagingConfig(pageSize = 20)) {
        if (query.isNotBlank()) {
            SearchNewsPagingSource(retrofitInstance, query)
        } else {
            NewsPagingSource(retrofitInstance)
        }
    }.flow

    fun searchHeadline(q: String){
        query = q
    }

    fun connectToDb(context: Context){
        db = MainDb.getDb(context)
    }

}