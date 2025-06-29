package com.example.remainderkesehatanpasien.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "checklist_items")
data class CheckListItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val isChecked: Boolean = false,
    val timestamp: Long
)