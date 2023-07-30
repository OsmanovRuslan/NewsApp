package com.training.newsapp.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.training.newsapp.dataclasses.Headline
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHeadline(headline: Headlines)

    @Query("SELECT * FROM headlines")
    fun getHeadlines() : Flow<List<Headlines>>

    @Query("SELECT * FROM headlines WHERE title = :title")
    fun getHeadline(title: String) : Headlines?

    @Query("DELETE FROM headlines WHERE title = :title")
    fun deleteHeadline(title: String)
}