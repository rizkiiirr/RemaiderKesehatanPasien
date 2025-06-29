package com.example.remainderkesehatanpasien.data.repository

import com.example.remainderkesehatanpasien.data.local.dao.NewsDao
import com.example.remainderkesehatanpasien.data.local.entity.Article
import com.example.remainderkesehatanpasien.data.remote.api.NewsApiService
import com.example.remainderkesehatanpasien.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val apiService: NewsApiService,
    private val newsDao: NewsDao
) : NewsRepository {

    private val API_KEY = "881322c0c0f64c2d812cb62c2e0d6860"

    override fun getHealthNews(query: String, apiKey: String): Flow<List<Article>> = flow{
        // untuk menampilkan data dari cache database dulu
        val cachedArticles = newsDao.getAllArticles().first()
        emit(cachedArticles)

        // mengambil data baru di latar belakang dan menghapus cache lama
        try {
            val response = apiService.getHealthNews(query = query, apiKey = API_KEY)
            if (response.status == "ok") {
                newsDao.deleteAllArticles()
                newsDao.insertArticles(response.articles)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}