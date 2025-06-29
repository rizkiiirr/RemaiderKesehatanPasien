package com.example.remainderkesehatanpasien.presentation.auth

import android.content.res.Resources
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.remainderkesehatanpasien.R
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
    private val sessionManager: SessionManager,
    private val resources: Resources

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
                    _uiEvent.emit(AuthUiEvent.ShowSnackbar(resources.getString(R.string.email_already_exists)))
                }
            } catch (e: InvalidInputException) {
                when (e.message) {
                    resources.getString(R.string.error_invalid_email) -> registerEmail = registerEmail.copy(error = resources.getString(R.string.error_invalid_email))
                    resources.getString(R.string.error_username_empty) -> registerUsername = registerUsername.copy(error = resources.getString(R.string.error_username_empty))
                    resources.getString(R.string.error_fullname_empty) -> registerFullName = registerFullName.copy(error = resources.getString(R.string.error_fullname_empty))
                    resources.getString(R.string.error_password_min_length) -> registerPassword = registerPassword.copy(error = resources.getString(R.string.error_password_min_length))
                    else -> _uiEvent.emit(AuthUiEvent.ShowSnackbar(resources.getString(R.string.error_registration_unknown)))
                }
            } catch (e: Exception) {
                _uiEvent.emit(AuthUiEvent.ShowSnackbar(resources.getString(R.string.error_unexpected, e.message ?: "Unknown error")))
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
                    _uiEvent.emit(AuthUiEvent.ShowSnackbar(resources.getString(R.string.error_invalid_credentials)))
                }
            } catch (e: InvalidInputException) {
                when (e.message) {
                    resources.getString(R.string.error_invalid_email) -> loginEmail = loginEmail.copy(error = resources.getString(R.string.error_invalid_email))
                    resources.getString(R.string.error_password_min_length) -> loginPassword = loginPassword.copy(error = resources.getString(R.string.error_password_min_length))
                    else -> _uiEvent.emit(AuthUiEvent.ShowSnackbar(resources.getString(R.string.error_login_unknown)))
                }
            } catch (e: Exception) {
                _uiEvent.emit(AuthUiEvent.ShowSnackbar(resources.getString(R.string.error_unexpected, e.message ?: "Unknown error")))
            }
        }
    }
    fun logoutUser() {
        sessionManager.logout()
    }
}
