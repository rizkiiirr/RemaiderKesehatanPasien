package com.example.remainderkesehatanpasien.data.repository

import com.example.remainderkesehatanpasien.data.local.dao.NewsDao
import com.example.remainderkesehatanpasien.data.remote.Article
import com.example.remainderkesehatanpasien.data.remote.api.NewsApiService
import com.example.remainderkesehatanpasien.data.remote.source.NewsRemoteDataSource
import com.example.remainderkesehatanpasien.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

// Implementasi nyata dari NewsRepository.
// Kelas ini tahu BAGAIMANA cara mendapatkan data berita (dalam hal ini dari remote data source).
class NewsRepositoryImpl @Inject constructor(
    private val apiService: NewsApiService,
    private val newsDao: NewsDao
) : NewsRepository {

    // API Key NewsAPI Anda.
    // PENTING: Untuk proyek sungguhan, jangan simpan API Key langsung di kode seperti ini.
    // Gunakan BuildConfig, string resources terenkripsi, atau cara yang lebih aman.
    // Untuk tujuan UAS dan demo, ini bisa diterima.
    private val API_KEY = "881322c0c0f64c2d812cb62c2e0d6860"

    override fun getHealthNews(query: String, apiKey: String): Flow<List<Article>> = flow{
        // 1. Tampilkan data dari cache (database) terlebih dahulu
        val cachedArticles = newsDao.getAllArticles().first() // Ambil data saat ini
        emit(cachedArticles)

        // 2. Coba ambil data baru dari network di latar belakang
        try {
            val response = apiService.getHealthNews(query = query, apiKey = API_KEY)
            if (response.status == "ok") {
                // 3. Jika berhasil, hapus cache lama dan simpan yang baru
                newsDao.deleteAllArticles()
                newsDao.insertArticles(response.articles)

                // 4. Emit data baru dari database (yang sekarang sudah terupdate)
                // Flow dari getAllArticles akan otomatis memancarkan data baru ini ke UI
                // 4. Emit data baru dari database (yang sekarang sudah terupdate)
                // Flow dari getAllArticles akan otomatis memancarkan data baru ini ke UI
            }
        } catch (e: Exception) {
            // Jika network gagal, tidak perlu melakukan apa-apa.
            // Pengguna akan tetap melihat data dari cache.
            // Kita bisa log error ini jika perlu.
            e.printStackTrace()
        }
    }
}