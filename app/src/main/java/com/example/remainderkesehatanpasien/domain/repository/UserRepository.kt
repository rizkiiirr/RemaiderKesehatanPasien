package com.example.remainderkesehatanpasien.domain.repository

import com.example.remainderkesehatanpasien.data.local.entity.User

// Antarmuka ini adalah kontrak untuk operasi data pengguna di lapisan Domain.
// Lapisan Domain hanya peduli dengan fungsionalitas, bukan detail implementasi.
interface UserRepository {
    suspend fun registerUser(user: User): Boolean // Mengembalikan true jika berhasil daftar, false jika email sudah ada
    suspend fun loginUser(email: String, passwordHash: String): User? // Mengembalikan User jika login berhasil
    suspend fun updateUser(user: User) // <-- TAMBAHKAN INI UNTUK UPDATE PROFIL
    suspend fun getUserById(email: String): User? // <-- TAMBAHKAN INI UNTUK MENGAMBIL DATA USER
}