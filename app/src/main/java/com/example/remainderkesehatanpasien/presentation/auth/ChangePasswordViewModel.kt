package com.example.remainderkesehatanpasien.presentation.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.remainderkesehatanpasien.domain.manager.SessionManager
import com.example.remainderkesehatanpasien.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


data class PasswordFieldState(
    val text: String = "",
    val error: String? = null
)

sealed class ChangePasswordUiEvent {
    data class ShowSnackbar(val message: String) : ChangePasswordUiEvent()
    object ChangePasswordSuccess : ChangePasswordUiEvent()
}

sealed class ChangePasswordFormEvent {
    data class EnteredCurrentPassword(val password: String) : ChangePasswordFormEvent()
    data class EnteredNewPassword(val password: String) : ChangePasswordFormEvent()
    data class EnteredConfirmNewPassword(val password: String) : ChangePasswordFormEvent()
    object SubmitChange : ChangePasswordFormEvent()
}

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    var currentPassword by mutableStateOf(PasswordFieldState())
        private set
    var newPassword by mutableStateOf(PasswordFieldState())
        private set
    var confirmNewPassword by mutableStateOf(PasswordFieldState())
        private set

    private val _uiEvent = MutableSharedFlow<ChangePasswordUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun onEvent(event: ChangePasswordFormEvent) {
        when (event) {
            is ChangePasswordFormEvent.EnteredCurrentPassword -> {
                currentPassword = currentPassword.copy(text = event.password, error = null)
            }
            is ChangePasswordFormEvent.EnteredNewPassword -> {
                newPassword = newPassword.copy(text = event.password, error = null)
            }
            is ChangePasswordFormEvent.EnteredConfirmNewPassword -> {
                confirmNewPassword = confirmNewPassword.copy(text = event.password, error = null)
            }
            ChangePasswordFormEvent.SubmitChange -> {
                changePassword()
            }
        }
    }

    private fun changePassword() {
        viewModelScope.launch {
            val userEmail = sessionManager.getCurrentUserEmail()
            if (userEmail == null) {
                _uiEvent.emit(ChangePasswordUiEvent.ShowSnackbar("Tidak ada pengguna yang login."))
                return@launch
            }

            // Dapatkan user dari database untuk verifikasi password saat ini
            val currentUserFromDb = userRepository.getUserById(userEmail)
            if (currentUserFromDb == null) {
                _uiEvent.emit(ChangePasswordUiEvent.ShowSnackbar("Pengguna tidak ditemukan di database."))
                return@launch
            }

            // Verifikasi password saat ini (Dalam aplikasi nyata, hash password saat ini dan bandingkan dengan yang tersimpan)
            if (currentPassword.text != currentUserFromDb.passwordHash) {
                currentPassword = currentPassword.copy(error = "Password saat ini salah.")
                _uiEvent.emit(ChangePasswordUiEvent.ShowSnackbar("Password saat ini salah."))
                return@launch
            }

            // Validasi password baru
            if (newPassword.text.isBlank() || newPassword.text.length < 6) {
                newPassword = newPassword.copy(error = "Password baru minimal 6 karakter.")
                _uiEvent.emit(ChangePasswordUiEvent.ShowSnackbar("Password baru minimal 6 karakter."))
                return@launch
            }
            if (newPassword.text != confirmNewPassword.text) {
                confirmNewPassword = confirmNewPassword.copy(error = "Password baru tidak cocok.")
                _uiEvent.emit(ChangePasswordUiEvent.ShowSnackbar("Konfirmasi password tidak cocok."))
                return@launch
            }
            if (newPassword.text == currentPassword.text) {
                newPassword = newPassword.copy(error = "Password baru tidak boleh sama dengan password lama.")
                _uiEvent.emit(ChangePasswordUiEvent.ShowSnackbar("Password baru tidak boleh sama dengan password lama."))
                return@launch
            }

            // Hash password baru sebelum menyimpan (PENTING untuk keamanan)
            val newPasswordHashed = newPassword.text // Ini HARUS di-hash di aplikasi nyata!

            val updatedUser = currentUserFromDb.copy(passwordHash = newPasswordHashed)

            try {
                userRepository.updateUser(updatedUser)
                sessionManager.setUser(updatedUser) // Perbarui sesi juga
                _uiEvent.emit(ChangePasswordUiEvent.ChangePasswordSuccess)
                _uiEvent.emit(ChangePasswordUiEvent.ShowSnackbar("Password berhasil diubah."))
            } catch (e: Exception) {
                _uiEvent.emit(ChangePasswordUiEvent.ShowSnackbar("Gagal mengubah password: ${e.message}"))
            }
        }
    }
}
