package com.example.remainderkesehatanpasien.presentation.news

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.remainderkesehatanpasien.data.remote.Article
import com.example.remainderkesehatanpasien.domain.usecase.GetHealthNewsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

// Definisi state untuk layar berita
data class NewsState(
    val articles: List<Article> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val getHealthNewsUseCase: GetHealthNewsUseCase // Injeksi Use Case
) : ViewModel() {

    // State yang akan diobservasi oleh UI
    private val _state = mutableStateOf(NewsState())
    val state: State<NewsState> = _state

    private var getNewsJob: Job? = null

    // API Key NewsAPI Anda.
    // PENTING: Untuk proyek sungguhan, jangan simpan API Key langsung di kode seperti ini.
    // Gunakan BuildConfig, string resources terenkripsi, atau cara yang lebih aman.
    // Untuk tujuan UAS dan demo, ini bisa diterima.
    private val API_KEY = "881322c0c0f64c2d812cb62c2e0d6860" // <-- GANTI DENGAN API KEY ANDA

    init {
        // Panggil saat ViewModel dibuat
        getHealthNews("kesehatan") // Kata kunci default
    }

    // Fungsi untuk mendapatkan berita kesehatan
    fun getHealthNews(query: String) {
        getNewsJob?.cancel() // Batalkan job sebelumnya jika ada

        getNewsJob = getHealthNewsUseCase(query, API_KEY) // Panggil Use Case
            .onEach { result ->
                // Perbarui state berdasarkan hasil
                _state.value = _state.value.copy(
                    articles = result, // Mengatur daftar artikel yang diterima
                    isLoading = false,
                    error = null // Menghapus error sebelumnya
                )
            }
            .launchIn(viewModelScope) // Jalankan flow dalam ViewModel scope
    }
}
