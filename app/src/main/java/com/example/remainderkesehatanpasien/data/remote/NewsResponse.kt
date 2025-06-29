package com.example.remainderkesehatanpasien.data.remote

data class NewsResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<Article>
)