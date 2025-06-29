package com.example.remainderkesehatanpasien.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.remainderkesehatanpasien.data.local.entity.Article
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticles(articles: List<Article>)

    @Query("SELECT * FROM articles")
    fun getAllArticles(): Flow<List<Article>>

    @Query("DELETE FROM articles")
    suspend fun deleteAllArticles()
}