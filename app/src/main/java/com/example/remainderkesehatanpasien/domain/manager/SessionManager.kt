package com.example.remainderkesehatanpasien.domain.manager

import com.example.remainderkesehatanpasien.data.local.entity.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor() {

    // MutableStateFlow untuk menyimpan pengguna yang sedang login
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    // Fungsi untuk mengatur pengguna yang sedang login (setelah login/registrasi berhasil)
    fun setUser(user: User?) {
        _currentUser.value = user
    }

    // Fungsi untuk mendapatkan email pengguna yang sedang login
    fun getCurrentUserEmail(): String? {
        return _currentUser.value?.email
    }

    // Fungsi untuk logout (menghapus pengguna dari sesi)
    fun logout() {
        _currentUser.value = null
    }
}