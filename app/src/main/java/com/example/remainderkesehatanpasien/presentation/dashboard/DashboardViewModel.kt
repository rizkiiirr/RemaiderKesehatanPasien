package com.example.remainderkesehatanpasien.presentation.dashboard

import androidx.lifecycle.ViewModel
import com.example.remainderkesehatanpasien.domain.manager.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    val sessionManager: SessionManager 
) : ViewModel() {

    val currentUser = sessionManager.currentUser 
}