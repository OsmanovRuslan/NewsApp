package com.training.newsapp.dataclasses

data class News(
    val status: String,
    val totalResults: Int,
    val articles: List<Headline>
)
