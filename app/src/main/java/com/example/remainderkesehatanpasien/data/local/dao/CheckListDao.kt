package com.example.remainderkesehatanpasien.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.remainderkesehatanpasien.data.local.entity.CheckListItem
import kotlinx.coroutines.flow.Flow

@Dao
interface CheckListDao{
    @Query("SELECT * FROM checklist_items ORDER BY id DESC")
    fun getAllCheckListItems(): Flow<List<CheckListItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCheckListItem(item: CheckListItem)

    @Update
    suspend fun updateCheckListItem(item: CheckListItem)

    @Delete
    suspend fun deleteCheckListItem(item: CheckListItem)
}