package com.example.remainderkesehatanpasien.data.remote

import com.example.remainderkesehatanpasien.data.local.entity.Article

data class NewsResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<Article>
)