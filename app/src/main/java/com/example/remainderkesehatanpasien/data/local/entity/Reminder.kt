package com.example.remainderkesehatanpasien.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reminders")
data class Reminder(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String?,
    val reminderTime: Long,
    val allDay: Boolean = false,
    val category: String,
)