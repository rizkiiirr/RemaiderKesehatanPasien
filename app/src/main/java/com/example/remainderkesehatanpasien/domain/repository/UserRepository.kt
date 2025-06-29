package com.example.remainderkesehatanpasien.domain.repository

import com.example.remainderkesehatanpasien.data.local.entity.User

interface UserRepository {
    suspend fun registerUser(user: User): Boolean
    suspend fun loginUser(email: String, passwordHash: String): User?
    suspend fun updateUser(user: User)
    suspend fun getUserById(email: String): User?
}