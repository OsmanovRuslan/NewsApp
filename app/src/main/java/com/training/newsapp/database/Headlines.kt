package com.training.newsapp.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.training.newsapp.retrofit.dataclasses.Headline
import com.training.newsapp.retrofit.dataclasses.Source
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "headlines")
data class Headlines(
    @ColumnInfo(name = "author")
    val author: String? = null,
    @ColumnInfo(name = "content")
    val content: String? = null,
    @ColumnInfo(name = "description")
    val description: String? = null,
    @ColumnInfo(name = "publishedAt")
    val publishedAt: String? = null,
    @ColumnInfo(name = "source_id")
    val sourceId: String? = null,
    @ColumnInfo(name = "source_name")
    val sourceName: String? = null,
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "url")
    val url: String? = null,
    @ColumnInfo(name = "urlToImage")
    val urlToImage: String? = null,

): Parcelable
