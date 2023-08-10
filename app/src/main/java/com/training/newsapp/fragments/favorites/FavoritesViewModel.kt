package com.training.newsapp.fragments.favorites

import android.content.Context
import androidx.lifecycle.ViewModel
import com.training.newsapp.database.Headlines
import com.training.newsapp.database.MainDb
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class FavoritesViewModel : ViewModel() {

    lateinit var db: MainDb

    val allHeadlinesFlow: Flow<List<Headlines>> by lazy {
        db.getDao().getHeadlines().flowOn(Dispatchers.IO)
    }

    fun connectToDb(context: Context){
        db = MainDb.getDb(context)
    }
}