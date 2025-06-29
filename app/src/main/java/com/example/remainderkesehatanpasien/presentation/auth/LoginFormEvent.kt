package com.example.remainderkesehatanpasien.presentation.auth

sealed class LoginFormEvent {
    data class EnteredEmail(val email: String) : LoginFormEvent()
    data class EnteredPassword(val password: String) : LoginFormEvent()
    object Login : LoginFormEvent()
}