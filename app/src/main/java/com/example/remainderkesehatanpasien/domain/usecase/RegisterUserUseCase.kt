package com.example.remainderkesehatanpasien.domain.usecase

import com.example.remainderkesehatanpasien.data.local.entity.User
import com.example.remainderkesehatanpasien.domain.InvalidInputException
import com.example.remainderkesehatanpasien.domain.repository.UserRepository

class RegisterUserUseCase(
    private val userRepository: UserRepository
) {
    @Throws(InvalidInputException::class) // Anotasi ini untuk menunjukkan pengecualian yang dilempar
    suspend operator fun invoke(email: String, username: String, fullName: String, passwordHash: String): Boolean {
        // Lakukan validasi input di sini
        if (email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            throw InvalidInputException("Email tidak valid.")
        }
        if (username.isBlank()) {
            throw InvalidInputException("Nama pengguna tidak boleh kosong.")
        }
        if (fullName.isBlank()) { // <-- Validasi fullName
            throw InvalidInputException("Nama lengkap tidak boleh kosong.")
        }
        if (passwordHash.length < 6) { // Asumsi passwordHash sudah di-hash (panjang minimal 6 karakter)
            throw InvalidInputException("Password minimal 6 karakter.")
        }

        val newUser = User(email = email, username = username, fullName = fullName, passwordHash = passwordHash)
        return userRepository.registerUser(newUser)
    }
}