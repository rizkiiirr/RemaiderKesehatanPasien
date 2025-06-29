package com.example.remainderkesehatanpasien.data.repository

import com.example.remainderkesehatanpasien.data.local.dao.UserDao
import com.example.remainderkesehatanpasien.data.local.entity.User
import com.example.remainderkesehatanpasien.domain.repository.UserRepository
import javax.inject.Inject

// Implementasi nyata dari UserRepository.
// Kelas ini tahu BAGAIMANA cara menyimpan dan mengambil data pengguna dari UserDao.
class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) : UserRepository {

    override suspend fun registerUser(user: User): Boolean {
        // Cek apakah email sudah terdaftar
        val existingUser = userDao.getUserByEmail(user.email)
        return if (existingUser == null) {
            userDao.insertUser(user)
            true // Pendaftaran berhasil
        } else {
            false // Email sudah terdaftar
        }
    }

    override suspend fun loginUser(email: String, passwordHash: String): User? {
        val user = userDao.getUserByEmail(email)
        // Verifikasi passwordHash. Dalam aplikasi nyata, gunakan bcrypt atau algoritma hashing yang kuat.
        return if (user != null && user.passwordHash == passwordHash) {
            user
        } else {
            null // Login gagal
        }
    }

    override suspend fun updateUser(user: User) { // <-- IMPLEMENTASI update User
        userDao.insertUser(user) // Dengan OnConflictStrategy.REPLACE, ini akan berfungsi sebagai update
    }

    override suspend fun getUserById(email: String): User? { // <-- IMPLEMENTASI getUserById
        return userDao.getUserByEmail(email)
    }
}
