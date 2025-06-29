package com.example.remainderkesehatanpasien.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.remainderkesehatanpasien.data.local.entity.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE) // Jika user dengan email yang sama ada, ganti
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM users_table WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?

    // Anda bisa tambahkan query lain seperti deleteUser, updatePassword, dll. jika diperlukan
}