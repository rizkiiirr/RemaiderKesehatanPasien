package com.example.remainderkesehatanpasien.data.remote.api

import com.example.remainderkesehatanpasien.data.remote.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {
    @GET("everything") // Atau "top-headlines" jika Anda hanya ingin berita utama
    suspend fun getHealthNews(
        @Query("q") query: String = "kesehatan", // Kata kunci pencarian (misal: "kesehatan", "gizi", "medis")
        @Query("language") language: String = "id", // Bahasa artikel (misal: "id" untuk Indonesia)
        @Query("apiKey") apiKey: String // API Key Anda
    ): NewsResponse
}