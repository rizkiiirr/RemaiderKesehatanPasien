package com.example.remainderkesehatanpasien.data.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.remainderkesehatanpasien.data.remote.source.Source

@Entity(tableName = "articles")
data class Article(
    @PrimaryKey
    val url: String,
    @Embedded
    val source: Source?,
    val author: String?,
    val title: String,
    val description: String?,
    val urlToImage: String?,
    val publishedAt: String,
    val content: String?
)