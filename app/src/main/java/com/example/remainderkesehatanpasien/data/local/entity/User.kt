package com.example.remainderkesehatanpasien.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users_table")
data class User(
    @PrimaryKey(autoGenerate = false) // Kita akan menggunakan email sebagai primary key, tidak auto-generate
    val email: String, // Email akan berfungsi sebagai ID unik untuk pengguna
    val username: String,
    val fullName: String,
    val passwordHash: String, // Simpan password yang sudah di-hash (penting untuk keamanan)
    val profileImageUrl: String? = null
)
