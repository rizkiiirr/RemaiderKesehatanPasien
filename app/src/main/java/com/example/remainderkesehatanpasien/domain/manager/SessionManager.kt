package com.example.remainderkesehatanpasien.domain.manager

import com.example.remainderkesehatanpasien.data.local.entity.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor() {

    // menyimpan pengguna yang login
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    fun setUser(user: User?) {
        _currentUser.value = user
    }

    fun getCurrentUserEmail(): String? {
        return _currentUser.value?.email
    }

    fun logout() {
        _currentUser.value = null
    }
}