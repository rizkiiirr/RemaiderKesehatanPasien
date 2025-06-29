package com.example.remainderkesehatanpasien.domain.usecase

import com.example.remainderkesehatanpasien.data.local.entity.User
import com.example.remainderkesehatanpasien.domain.InvalidInputException
import com.example.remainderkesehatanpasien.domain.repository.UserRepository

class LoginUserUseCase(
    private val userRepository: UserRepository
) {
    @Throws(InvalidInputException::class)
    suspend operator fun invoke(email: String, passwordHash: String): User? {
        // Lakukan validasi input di sini
        if (email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            throw InvalidInputException("Email tidak valid.")
        }
        if (passwordHash.length < 6) { // Asumsi passwordHash sudah di-hash (panjang minimal 6 karakter)
            throw InvalidInputException("Password minimal 6 karakter.")
        }
        return userRepository.loginUser(email, passwordHash)
    }
}