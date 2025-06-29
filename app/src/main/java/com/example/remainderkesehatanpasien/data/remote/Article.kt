package com.example.remainderkesehatanpasien.data.remote

data class Article(
    val source: Source?, // Bisa null
    val author: String?, // Bisa null
    val title: String,
    val description: String?, // Bisa null
    val url: String,
    val urlToImage: String?, // Bisa null
    val publishedAt: String,
    val content: String? // Bisa null
)