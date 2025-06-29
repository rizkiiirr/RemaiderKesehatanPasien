package com.example.remainderkesehatanpasien.data.repository

import com.example.remainderkesehatanpasien.data.local.dao.CheckListDao
import com.example.remainderkesehatanpasien.data.local.entity.CheckListItem
import com.example.remainderkesehatanpasien.domain.repository.CheckListRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CheckListRepositoryImpl @Inject constructor(
    private val dao: CheckListDao
) : CheckListRepository {
    override fun getAllItems(): Flow<List<CheckListItem>> {
        return dao.getAllCheckListItems()
    }

    override suspend fun insertItem(item: CheckListItem){
        dao.insertCheckListItem(item)
    }

    override suspend fun updateItem(item: CheckListItem){
        dao.updateCheckListItem(item)
    }

    override suspend fun deleteItem(item: CheckListItem){
        dao.deleteCheckListItem(item)
    }
}