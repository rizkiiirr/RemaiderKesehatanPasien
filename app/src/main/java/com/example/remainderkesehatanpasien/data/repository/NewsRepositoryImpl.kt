package com.example.remainderkesehatanpasien.data.repository

import com.example.remainderkesehatanpasien.data.remote.Article
import com.example.remainderkesehatanpasien.data.remote.source.NewsRemoteDataSource
import com.example.remainderkesehatanpasien.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

// Implementasi nyata dari NewsRepository.
// Kelas ini tahu BAGAIMANA cara mendapatkan data berita (dalam hal ini dari remote data source).
class NewsRepositoryImpl @Inject constructor(
    private val remoteDataSource: NewsRemoteDataSource
) : NewsRepository {

    // API Key NewsAPI Anda.
    // PENTING: Untuk proyek sungguhan, jangan simpan API Key langsung di kode seperti ini.
    // Gunakan BuildConfig, string resources terenkripsi, atau cara yang lebih aman.
    // Untuk tujuan UAS dan demo, ini bisa diterima.
    private val API_KEY = "881322c0c0f64c2d812cb62c2e0d6860"

    override fun getHealthNews(query: String, apiKey: String): Flow<List<Article>> = flow{
        try{
            val response = remoteDataSource.getHealthNews(query, apiKey)
            // Emit daftar artikel jika status respons adalah "ok"
            if (response.status == "ok"){
                emit(response.articles)
            } else{
                emit(emptyList())
            }
        } catch (e: HttpException){
            // Error dari server (misal: 4xx, 5xx)
            // Anda bisa mengirimkan pesan error yang lebih spesifik
            emit(emptyList())// Emit kosong saat terjadi error HTTP
            e.printStackTrace() // Cetak stack trace untuk debugging
        } catch (e: IOException) {
            // Error koneksi jaringan (misal: tidak ada internet)
            emit(emptyList()) // Emit kosong saat tidak ada koneksi
            e.printStackTrace() // Cetak stack trace untuk debugging
        } catch (e: Exception) {
            // Error lain yang tidak terduga
            emit(emptyList()) // Emit kosong untuk error lain
            e.printStackTrace() // Cetak stack trace untuk debugging
        }
    }
}