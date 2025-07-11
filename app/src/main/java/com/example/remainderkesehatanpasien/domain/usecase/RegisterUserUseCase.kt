package com.example.remainderkesehatanpasien.domain.usecase

import com.example.remainderkesehatanpasien.data.local.entity.User
import com.example.remainderkesehatanpasien.domain.InvalidInputException
import com.example.remainderkesehatanpasien.domain.repository.UserRepository

class RegisterUserUseCase(
    private val userRepository: UserRepository
) {
    @Throws(InvalidInputException::class)
    suspend operator fun invoke(email: String, username: String, fullName: String, passwordHash: String): Boolean {

        if (email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            throw InvalidInputException("Email tidak valid.")
        }
        if (username.isBlank()) {
            throw InvalidInputException("Nama pengguna tidak boleh kosong.")
        }
        if (fullName.isBlank()) {
            throw InvalidInputException("Nama lengkap tidak boleh kosong.")
        }
        if (passwordHash.length < 6) {
            throw InvalidInputException("Password minimal 6 karakter.")
        }

        val newUser = User(email = email, username = username, fullName = fullName, passwordHash = passwordHash)
        return userRepository.registerUser(newUser)
    }
}