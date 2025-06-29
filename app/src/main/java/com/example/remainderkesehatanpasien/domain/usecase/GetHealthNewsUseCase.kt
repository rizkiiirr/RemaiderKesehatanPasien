package com.example.remainderkesehatanpasien.domain.usecase

import com.example.remainderkesehatanpasien.data.remote.Article
import com.example.remainderkesehatanpasien.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

// Use Case ini bertanggung jawab untuk mendapatkan daftar berita kesehatan.
// Ini mengabstraksi detail dari lapisan Repository.
class GetHealthNewsUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    // 'operator fun invoke' memungkinkan kita memanggil class ini seolah-olah sebuah fungsi
    // contoh: getHealthNewsUseCase("kesehatan")
    operator fun invoke(query: String, apiKey: String): Flow<List<Article>> {
        return repository.getHealthNews(query, apiKey)
    }
}