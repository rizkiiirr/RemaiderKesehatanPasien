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
import com.example.remainderkesehatanpasien.util.ImageStorageManager // Import ImageStorageManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

// Event yang dikirim dari ViewModel ke UI
sealed class ProfileUiEvent {
    data class ShowSnackbar(val message: String) : ProfileUiEvent()
    object SaveProfileSuccess : ProfileUiEvent()
}

// Event untuk ProfileForm
sealed class ProfileFormEvent {
    data class EnteredName(val value: String) : ProfileFormEvent()
    data class EnteredUsername(val value: String) : ProfileFormEvent()
    data class EnteredEmail(val value: String) : ProfileFormEvent()
    data class UpdateProfileImage(val imageUrl: String?) : ProfileFormEvent() // Ini akan menerima Uri.toString() langsung dari UI
    object SaveProfile : ProfileFormEvent()
}

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager,
    private val imageStorageManager: ImageStorageManager // <-- INJEKSI ImageStorageManager
) : ViewModel() {

    // State untuk data profil yang akan ditampilkan di UI
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
    private var originalProfileImageUrl: String? = null // Untuk melacak URL gambar asli untuk penghapusan

    init {
        // Observasi currentUser dari AuthViewModel
        viewModelScope.launch {
            sessionManager.currentUser.collectLatest { user ->
                user?.let {
                    currentUserEmail = it.email
                    name = it.fullName
                    username = it.username
                    email = it.email
                    profileImageUrl = it.profileImageUrl
                    originalProfileImageUrl = it.profileImageUrl // Simpan URL asli saat ini
                } ?: run {
                    // Jika tidak ada user (misal: logout), kosongkan profil
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
                // Saat event UpdateProfileImage diterima, simpan gambar ke internal storage
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

                    // Logika simpan gambar
                    event.imageUrl?.let { uriString ->
                        val savedUri = imageStorageManager.saveImageFromUri(android.net.Uri.parse(uriString))
                        if (savedUri != null) {
                            // Hapus gambar lama jika ada dan berbeda dari yang baru
                            if (originalProfileImageUrl != null && originalProfileImageUrl != savedUri.toString()) {
                                imageStorageManager.deleteImage(originalProfileImageUrl)
                            }
                            profileImageUrl = savedUri.toString() // Perbarui state ViewModel
                            _uiEvent.emit(ProfileUiEvent.ShowSnackbar("Gambar profil berhasil diperbarui."))
                        } else {
                            _uiEvent.emit(ProfileUiEvent.ShowSnackbar("Gagal menyimpan gambar profil."))
                        }
                    } ?: run {
                        // Jika URL gambar kosong, berarti pengguna menghapusnya atau tidak memilih
                        if (originalProfileImageUrl != null) {
                            imageStorageManager.deleteImage(originalProfileImageUrl)
                        }
                        profileImageUrl = null
                        _uiEvent.emit(ProfileUiEvent.ShowSnackbar("Gambar profil dihapus."))
                        // Perbarui juga di database setelah profileImageUrl di state ViewModel diperbarui
                        val finalUpdatedUser = userToUpdate.copy(
                            fullName = name, // Ambil dari state ViewModel
                            username = username, // Ambil dari state ViewModel
                            profileImageUrl = profileImageUrl // Ambil dari state ViewModel
                        )
                        try {
                            userRepository.updateUser(finalUpdatedUser)
                            // Perbarui juga di SessionManager agar UI yang lain segera merefleksikan perubahan
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
                // Ambil user terbaru dari database untuk memastikan kita update objek yang lengkap
                val existingUser = userRepository.getUserById(currentEmail)
                if (existingUser == null) {
                    _uiEvent.emit(ProfileUiEvent.ShowSnackbar("Pengguna tidak ditemukan."))
                    return@launch
                }
                val updatedUser = existingUser.copy( // Gunakan copy untuk mempertahankan passwordHash, dll.
                    username = username,
                    fullName = name,
                    profileImageUrl = profileImageUrl // <--- PASTIKAN INI DIAMBIL DARI STATE VIEWMODEL
                    // profileImageUrl sudah diupdate di onEvent UpdateProfileImage
                    // profileImageUrl = profileImageUrl, // Pastikan ini juga diset dari state
                    // email tidak bisa diubah karena primary key
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
