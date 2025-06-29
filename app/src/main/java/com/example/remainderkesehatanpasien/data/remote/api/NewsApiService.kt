package com.example.remainderkesehatanpasien.data.remote.api

import com.example.remainderkesehatanpasien.data.remote.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {
    @GET("everything")
    suspend fun getHealthNews(
        @Query("q") query: String = "kesehatan", // kata kunci berita
        @Query("language") language: String = "id", // bahasa artikel
        @Query("apiKey") apiKey: String
    ): NewsResponse
}