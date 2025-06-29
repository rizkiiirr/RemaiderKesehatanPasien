package com.example.remainderkesehatanpasien.presentation.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.remainderkesehatanpasien.domain.InvalidInputException
import com.example.remainderkesehatanpasien.domain.manager.SessionManager
import com.example.remainderkesehatanpasien.domain.usecase.LoginUserUseCase
import com.example.remainderkesehatanpasien.domain.usecase.RegisterUserUseCase
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// state input email dan password
data class AuthTextFieldState(
    val text: String = "",
    val error: String? = null
)

// event yang dikirim dari viewmodel ke ui
sealed class AuthUiEvent {
    data class ShowSnackbar(val message: String) : AuthUiEvent()
    object RegistrationSuccess : AuthUiEvent()
    object LoginSuccess : AuthUiEvent()
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val registerUserUseCase: RegisterUserUseCase,
    private val loginUserUseCase: LoginUserUseCase,
    private val sessionManager: SessionManager
) : ViewModel() {

    var registerEmail by mutableStateOf(AuthTextFieldState())
        private set
    var registerUsername by mutableStateOf(AuthTextFieldState())
        private set
    var registerFullName by mutableStateOf(AuthTextFieldState())
        private set
    var registerPassword by mutableStateOf(AuthTextFieldState())
        private set

    var loginEmail by mutableStateOf(AuthTextFieldState())
        private set
    var loginPassword by mutableStateOf(AuthTextFieldState())
        private set

    private val _uiEvent = MutableSharedFlow<AuthUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun onRegisterEvent(event: RegisterFormEvent) {
        when (event) {
            is RegisterFormEvent.EnteredEmail -> {
                registerEmail = registerEmail.copy(text = event.email, error = null)
            }
            is RegisterFormEvent.EnteredUsername -> {
                registerUsername = registerUsername.copy(text = event.username, error = null)
            }
            is RegisterFormEvent.EnteredFullName -> {
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

    private fun registerUser() {
        viewModelScope.launch {
            try {
                val passwordHash = registerPassword.text

                val isRegistered = registerUserUseCase(
                    email = registerEmail.text,
                    username = registerUsername.text,
                    fullName = registerFullName.text,
                    passwordHash = passwordHash
                )

                if (isRegistered) {
                    val registeredUser = loginUserUseCase(registerEmail.text, passwordHash)
                    sessionManager.setUser(registeredUser)
                    _uiEvent.emit(AuthUiEvent.RegistrationSuccess)
                } else {
                    _uiEvent.emit(AuthUiEvent.ShowSnackbar("Email sudah terdaftar!"))
                }
            } catch (e: InvalidInputException) {
                when (e.message) {
                    "Email tidak valid." -> registerEmail = registerEmail.copy(error = e.message)
                    "Nama pengguna tidak boleh kosong." -> registerUsername = registerUsername.copy(error = e.message)
                    "Nama lengkap tidak boleh kosong." -> registerFullName = registerFullName.copy(error = e.message)
                    "Password minimal 6 karakter." -> registerPassword = registerPassword.copy(error = e.message)
                    else -> _uiEvent.emit(AuthUiEvent.ShowSnackbar(e.message ?: "Terjadi kesalahan saat registrasi."))
                }
            } catch (e: Exception) {
                _uiEvent.emit(AuthUiEvent.ShowSnackbar("Terjadi kesalahan tak terduga: ${e.message}"))
            }
        }
    }

    private fun loginUser() {
        viewModelScope.launch {
            try {
                val passwordHash = loginPassword.text
                val user = loginUserUseCase(
                    email = loginEmail.text,
                    passwordHash = passwordHash
                )

                if (user != null) {
                    Firebase.analytics.logEvent("user_login_success", null)
                    sessionManager.setUser(user)
                    _uiEvent.emit(AuthUiEvent.LoginSuccess)
                } else {
                    _uiEvent.emit(AuthUiEvent.ShowSnackbar("Email atau password salah!"))
                }
            } catch (e: InvalidInputException) {
                when (e.message) {
                    "Email tidak valid." -> loginEmail = loginEmail.copy(error = e.message)
                    "Password minimal 6 karakter." -> loginPassword = loginPassword.copy(error = e.message)
                    else -> _uiEvent.emit(AuthUiEvent.ShowSnackbar(e.message ?: "Terjadi kesalahan saat login."))
                }
            } catch (e: Exception) {
                _uiEvent.emit(AuthUiEvent.ShowSnackbar("Terjadi kesalahan tak terduga: ${e.message}"))
            }
        }
    }
    fun logoutUser() {
        sessionManager.logout()
    }
}
