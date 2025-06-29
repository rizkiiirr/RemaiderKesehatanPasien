package com.example.remainderkesehatanpasien.presentation.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.remainderkesehatanpasien.data.local.entity.User
import com.example.remainderkesehatanpasien.domain.InvalidInputException
import com.example.remainderkesehatanpasien.domain.manager.SessionManager
import com.example.remainderkesehatanpasien.domain.usecase.LoginUserUseCase
import com.example.remainderkesehatanpasien.domain.usecase.RegisterUserUseCase
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// State untuk input email dan password
data class AuthTextFieldState(
    val text: String = "",
    val error: String? = null
)

// Event yang dikirim dari ViewModel ke UI (misal: sukses/gagal login/register, tampilkan Snackbar)
sealed class AuthUiEvent {
    data class ShowSnackbar(val message: String) : AuthUiEvent()
    object RegistrationSuccess : AuthUiEvent()
    object LoginSuccess : AuthUiEvent()
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val registerUserUseCase: RegisterUserUseCase,
    private val loginUserUseCase: LoginUserUseCase,
    private val sessionManager: SessionManager // <-- INJEKSI SessionManager
) : ViewModel() {

    // State untuk layar Register
    var registerEmail by mutableStateOf(AuthTextFieldState())
        private set
    var registerUsername by mutableStateOf(AuthTextFieldState())
        private set
    var registerFullName by mutableStateOf(AuthTextFieldState()) // <-- TAMBAHKAN INI (Nama Lengkap)
        private set
    var registerPassword by mutableStateOf(AuthTextFieldState())
        private set

    // State untuk layar Login
    var loginEmail by mutableStateOf(AuthTextFieldState())
        private set
    var loginPassword by mutableStateOf(AuthTextFieldState())
        private set

    // SharedFlow untuk event satu kali ke UI (misal: menampilkan Toast/Snackbar)
    private val _uiEvent = MutableSharedFlow<AuthUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    // Fungsi untuk mengubah input di Register Screen
    fun onRegisterEvent(event: RegisterFormEvent) {
        when (event) {
            is RegisterFormEvent.EnteredEmail -> {
                registerEmail = registerEmail.copy(text = event.email, error = null)
            }
            is RegisterFormEvent.EnteredUsername -> {
                registerUsername = registerUsername.copy(text = event.username, error = null)
            }
            is RegisterFormEvent.EnteredFullName -> { // <-- Handle event fullName
                registerFullName = registerFullName.copy(text = event.fullName, error = null)
            }
            is RegisterFormEvent.EnteredPassword -> {
                registerPassword = registerPassword.copy(text = event.password, error = null)
            }
            RegisterFormEvent.Register -> {
                registerUser()
            }
        }
    }

    // Fungsi untuk mengubah input di Login Screen
    fun onLoginEvent(event: LoginFormEvent) {
        when (event) {
            is LoginFormEvent.EnteredEmail -> {
                loginEmail = loginEmail.copy(text = event.email, error = null)
            }
            is LoginFormEvent.EnteredPassword -> {
                loginPassword = loginPassword.copy(text = event.password, error = null)
            }
            LoginFormEvent.Login -> {
                loginUser()
            }
        }
    }

    // --- Logika Bisnis untuk Registrasi ---
    private fun registerUser() {
        viewModelScope.launch {
            try {
                // Dalam aplikasi nyata, password harus di-hash sebelum disimpan
                // Untuk demo, kita gunakan password plain-text (TIDAK AMAN UNTUK PRODUKSI)
                val passwordHash = registerPassword.text // Ini HARUS di-hash di aplikasi nyata!

                val isRegistered = registerUserUseCase(
                    email = registerEmail.text,
                    username = registerUsername.text,
                    fullName = registerFullName.text,
                    passwordHash = passwordHash
                )

                if (isRegistered) {
                    // Setelah registrasi sukses, secara otomatis "login" pengguna baru
                    val registeredUser = loginUserUseCase(registerEmail.text, passwordHash)
                    sessionManager.setUser(registeredUser)
                    _uiEvent.emit(AuthUiEvent.RegistrationSuccess)
                } else {
                    _uiEvent.emit(AuthUiEvent.ShowSnackbar("Email sudah terdaftar!"))
                }
            } catch (e: InvalidInputException) {
                // Tangani error validasi input
                when (e.message) {
                    "Email tidak valid." -> registerEmail = registerEmail.copy(error = e.message)
                    "Nama pengguna tidak boleh kosong." -> registerUsername = registerUsername.copy(error = e.message)
                    "Nama lengkap tidak boleh kosong." -> registerFullName = registerFullName.copy(error = e.message) // <-- Tangani error fullName
                    "Password minimal 6 karakter." -> registerPassword = registerPassword.copy(error = e.message)
                    else -> _uiEvent.emit(AuthUiEvent.ShowSnackbar(e.message ?: "Terjadi kesalahan saat registrasi."))
                }
            } catch (e: Exception) {
                // Tangani error umum lainnya
                _uiEvent.emit(AuthUiEvent.ShowSnackbar("Terjadi kesalahan tak terduga: ${e.message}"))
            }
        }
    }

    // --- Logika Bisnis untuk Login ---
    private fun loginUser() {
        viewModelScope.launch {
            try {
                // Dalam aplikasi nyata, password harus di-hash untuk perbandingan
                // Untuk demo, kita gunakan password plain-text (TIDAK AMAN UNTUK PRODUKSI)
                val passwordHash = loginPassword.text // Ini HARUS di-hash di aplikasi nyata!

                val user = loginUserUseCase(
                    email = loginEmail.text,
                    passwordHash = passwordHash
                )

                if (user != null) {
                    Firebase.analytics.logEvent("user_login_success", null)
                    sessionManager.setUser(user) // Simpan pengguna yang login
                    _uiEvent.emit(AuthUiEvent.LoginSuccess)
                } else {
                    _uiEvent.emit(AuthUiEvent.ShowSnackbar("Email atau password salah!"))
                }
            } catch (e: InvalidInputException) {
                // Tangani error validasi input
                when (e.message) {
                    "Email tidak valid." -> loginEmail = loginEmail.copy(error = e.message)
                    "Password minimal 6 karakter." -> loginPassword = loginPassword.copy(error = e.message)
                    else -> _uiEvent.emit(AuthUiEvent.ShowSnackbar(e.message ?: "Terjadi kesalahan saat login."))
                }
            } catch (e: Exception) {
                // Tangani error umum lainnya
                _uiEvent.emit(AuthUiEvent.ShowSnackbar("Terjadi kesalahan tak terduga: ${e.message}"))
            }
        }
    }
    // Tambahkan fungsi untuk membersihkan current user saat logout (jika Anda memiliki fitur logout)
    fun logoutUser() {
        sessionManager.logout()
    }// <-- GUNAKAN SessionManager UNTUK LOGOUT    }
}
