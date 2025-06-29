package com.example.remainderkesehatanpasien.data.remote

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "articles")
data class Article(
    @PrimaryKey
    val url: String,
    @Embedded
    val source: Source?, // Bisa null
    val author: String?, // Bisa null
    val title: String,
    val description: String?, // Bisa null
    val urlToImage: String?, // Bisa null
    val publishedAt: String,
    val content: String? // Bisa null
)