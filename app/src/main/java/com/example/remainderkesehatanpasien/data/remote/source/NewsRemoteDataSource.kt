package com.example.remainderkesehatanpasien.data.remote.source

import com.example.remainderkesehatanpasien.data.remote.NewsResponse
import com.example.remainderkesehatanpasien.data.remote.api.NewsApiService
import javax.inject.Inject

class NewsRemoteDataSource @Inject constructor(
    private val newsApiService: NewsApiService
){
    suspend fun getHealthNews(query: String, apiKey: String): NewsResponse {
        return newsApiService.getHealthNews(query = query, apiKey = apiKey)
    }
}

// Melakukan panggilan API dan mengembalikan respons
// Di sini Anda bisa menambahkan penanganan error dasar jika diperlukan,
// meskipun biasanya lebih baik ditangani di lapisan Repository atau Use