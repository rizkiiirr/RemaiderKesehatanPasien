package com.example.remainderkesehatanpasien.presentation.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.remainderkesehatanpasien.data.local.entity.User
import com.example.remainderkesehatanpasien.domain.manager.SessionManager
import com.example.remainderkesehatanpasien.domain.repository.UserRepository
import com.example.remainderkesehatanpasien.presentation.auth.AuthViewModel
import com.example.remainderkesehatanpasien.util.ImageStorageManager 
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed class ProfileUiEvent {
    data class ShowSnackbar(val message: String) : ProfileUiEvent()
    object SaveProfileSuccess : ProfileUiEvent()
}


sealed class ProfileFormEvent {
    data class EnteredName(val value: String) : ProfileFormEvent()
    data class EnteredUsername(val value: String) : ProfileFormEvent()
    data class EnteredEmail(val value: String) : ProfileFormEvent()
    data class UpdateProfileImage(val imageUrl: String?) : ProfileFormEvent() 
    object SaveProfile : ProfileFormEvent()
}

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager,
    private val imageStorageManager: ImageStorageManager 
) : ViewModel() {

    
    var name by mutableStateOf("")
        private set
    var username by mutableStateOf("")
        private set
    var email by mutableStateOf("")
        private set
    var profileImageUrl by mutableStateOf<String?>(null)
        private set

    private val _uiEvent = MutableSharedFlow<ProfileUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private var currentUserEmail: String? = null
    private var originalProfileImageUrl: String? = null 

    init {
        
        viewModelScope.launch {
            sessionManager.currentUser.collectLatest { user ->
                user?.let {
                    currentUserEmail = it.email
                    name = it.fullName
                    username = it.username
                    email = it.email
                    profileImageUrl = it.profileImageUrl
                    originalProfileImageUrl = it.profileImageUrl 
                } ?: run {
                    
                    currentUserEmail = null
                    name = ""
                    username = ""
                    email = ""
                    profileImageUrl = null
                    originalProfileImageUrl = null
                }
            }
        }
    }

    fun onEvent(event: ProfileFormEvent) {
        when (event) {
            is ProfileFormEvent.EnteredName -> name = event.value
            is ProfileFormEvent.EnteredUsername -> username = event.value
            is ProfileFormEvent.EnteredEmail -> email = event.value
            is ProfileFormEvent.UpdateProfileImage -> {
                
                viewModelScope.launch {
                    val currentEmail = currentUserEmail ?: run {
                        _uiEvent.emit(ProfileUiEvent.ShowSnackbar("Tidak ada pengguna yang login."))
                        return@launch
                    }

                    val userToUpdate = userRepository.getUserById(currentEmail)
                    if (userToUpdate == null) {
                        _uiEvent.emit(ProfileUiEvent.ShowSnackbar("Pengguna tidak ditemukan."))
                        return@launch
                    }

                    
                    event.imageUrl?.let { uriString ->
                        val savedUri = imageStorageManager.saveImageFromUri(android.net.Uri.parse(uriString))
                        if (savedUri != null) {
                            
                            if (originalProfileImageUrl != null && originalProfileImageUrl != savedUri.toString()) {
                                imageStorageManager.deleteImage(originalProfileImageUrl)
                            }
                            profileImageUrl = savedUri.toString() 
                            _uiEvent.emit(ProfileUiEvent.ShowSnackbar("Gambar profil berhasil diperbarui."))
                        } else {
                            _uiEvent.emit(ProfileUiEvent.ShowSnackbar("Gagal menyimpan gambar profil."))
                        }
                    } ?: run {
                        
                        if (originalProfileImageUrl != null) {
                            imageStorageManager.deleteImage(originalProfileImageUrl)
                        }
                        profileImageUrl = null
                        _uiEvent.emit(ProfileUiEvent.ShowSnackbar("Gambar profil dihapus."))
                        
                        val finalUpdatedUser = userToUpdate.copy(
                            fullName = name, 
                            username = username, 
                            profileImageUrl = profileImageUrl 
                        )
                        try {
                            userRepository.updateUser(finalUpdatedUser)
                            
                            sessionManager.setUser(finalUpdatedUser)
                        } catch (e: Exception) {
                            _uiEvent.emit(ProfileUiEvent.ShowSnackbar("Gagal menyimpan perubahan gambar ke database: ${e.message}"))
                        }
                    }
                }
            }
            ProfileFormEvent.SaveProfile -> saveProfile()
        }
    }

    private fun saveProfile() {
        viewModelScope.launch {
            currentUserEmail?.let { currentEmail ->
                
                val existingUser = userRepository.getUserById(currentEmail)
                if (existingUser == null) {
                    _uiEvent.emit(ProfileUiEvent.ShowSnackbar("Pengguna tidak ditemukan."))
                    return@launch
                }
                val updatedUser = existingUser.copy( 
                    username = username,
                    fullName = name,
                    profileImageUrl = profileImageUrl 
                    
                    
                    
                )
                try {
                    userRepository.updateUser(updatedUser)
                    sessionManager.setUser(updatedUser)
                    _uiEvent.emit(ProfileUiEvent.ShowSnackbar("Profil berhasil diperbarui!"))
                    _uiEvent.emit(ProfileUiEvent.SaveProfileSuccess)
                } catch (e: Exception) {
                    _uiEvent.emit(ProfileUiEvent.ShowSnackbar("Gagal memperbarui profil: ${e.message}"))
                }
            } ?: run {
                _uiEvent.emit(ProfileUiEvent.ShowSnackbar("Tidak ada pengguna yang login untuk disimpan."))
            }
        }
    }
}
