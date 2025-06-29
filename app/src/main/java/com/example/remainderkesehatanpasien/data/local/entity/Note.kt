package com.example.remainderkesehatanpasien.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes_table") // Anotasi untuk menandai sebagai tabel
data class Note(
    @PrimaryKey(autoGenerate = true) // Anotasi untuk kunci utama
    val id: Int = 0,
    val title: String,
    val description: String,
    val timestamp: Long
)
