package com.example.remainderkesehatanpasien.domain.repository

import com.example.remainderkesehatanpasien.data.local.entity.Article
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    fun getHealthNews(query: String, apiKey: String): Flow<List<Article>>
}