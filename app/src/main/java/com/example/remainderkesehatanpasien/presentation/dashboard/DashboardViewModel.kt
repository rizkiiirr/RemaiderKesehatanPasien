package com.example.remainderkesehatanpasien.presentation.dashboard

import androidx.lifecycle.ViewModel
import com.example.remainderkesehatanpasien.domain.manager.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    val sessionManager: SessionManager // Menginjeksi SessionManager di ViewModel
) : ViewModel() {
    // ViewModel ini bisa digunakan untuk logika khusus Dashboard lainnya
    // Seperti pengelolaan state UI, dll.
    val currentUser = sessionManager.currentUser // Mengekspos currentUser dari SessionManager
}