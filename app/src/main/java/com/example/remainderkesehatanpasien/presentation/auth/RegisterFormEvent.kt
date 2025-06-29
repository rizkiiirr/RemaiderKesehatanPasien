package com.example.remainderkesehatanpasien.presentation.auth

sealed class RegisterFormEvent {
    data class EnteredEmail(val email: String) : RegisterFormEvent()
    data class EnteredUsername(val username: String) : RegisterFormEvent()
    data class EnteredFullName(val fullName: String) : RegisterFormEvent()
    data class EnteredPassword(val password: String) : RegisterFormEvent()
    object Register : RegisterFormEvent()
}

