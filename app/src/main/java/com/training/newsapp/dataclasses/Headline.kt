package com.training.newsapp.dataclasses

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Headline(
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    val source: Source?,
    val title: String,
    val url: String?,
    val urlToImage: String?
): Parcelable