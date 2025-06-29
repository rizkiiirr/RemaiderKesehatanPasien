package com.example.remainderkesehatanpasien.domain.repository

import com.example.remainderkesehatanpasien.data.local.entity.CheckListItem
import kotlinx.coroutines.flow.Flow

interface CheckListRepository{
    fun getAllItems(): Flow<List<CheckListItem>>
    suspend fun insertItem(item: CheckListItem)
    suspend fun updateItem(item: CheckListItem)
    suspend fun deleteItem(item: CheckListItem)
}