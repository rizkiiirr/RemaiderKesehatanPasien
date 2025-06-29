package com.example.remainderkesehatanpasien.data.repository

import com.example.remainderkesehatanpasien.data.local.dao.UserDao
import com.example.remainderkesehatanpasien.data.local.entity.User
import com.example.remainderkesehatanpasien.domain.repository.UserRepository
import javax.inject.Inject


class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) : UserRepository {

    override suspend fun registerUser(user: User): Boolean {
        val existingUser = userDao.getUserByEmail(user.email) // mengecek apakah email sudah terdaftar atau belum
        return if (existingUser == null) {
            userDao.insertUser(user)
            true
        } else {
            false
        }
    }

    override suspend fun loginUser(email: String, passwordHash: String): User? {
        val user = userDao.getUserByEmail(email)
        return if (user != null && user.passwordHash == passwordHash) {
            user
        } else {
            null
        }
    }

    override suspend fun updateUser(user: User) {
        userDao.insertUser(user)
    }

    override suspend fun getUserById(email: String): User? { 
        return userDao.getUserByEmail(email)
    }
}
