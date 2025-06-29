package com.example.remainderkesehatanpasien.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users_table")
data class User(
    @PrimaryKey(autoGenerate = false)
    val email: String,
    val username: String,
    val fullName: String,
    val passwordHash: String,
    val profileImageUrl: String? = null
)
