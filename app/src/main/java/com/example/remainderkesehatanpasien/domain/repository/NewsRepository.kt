package com.example.remainderkesehatanpasien.domain.repository

import com.example.remainderkesehatanpasien.data.remote.Article
import kotlinx.coroutines.flow.Flow

// Antarmuka ini adalah kontrak untuk operasi data berita di lapisan Domain.
// Lapisan Domain hanya peduli dengan fungsionalitas, bukan detail implementasi (misal: dari mana data didapat).
interface NewsRepository {
    fun getHealthNews(query: String, apiKey: String): Flow<List<Article>>
}