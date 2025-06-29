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