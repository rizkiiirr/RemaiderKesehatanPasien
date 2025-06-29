package com.example.remainderkesehatanpasien.domain.usecase

import com.example.remainderkesehatanpasien.data.local.entity.Article
import com.example.remainderkesehatanpasien.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetHealthNewsUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    operator fun invoke(query: String, apiKey: String): Flow<List<Article>> {
        return repository.getHealthNews(query, apiKey)
    }
}