package com.example.remainderkesehatanpasien.presentation.news

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.remainderkesehatanpasien.data.local.entity.Article
import com.example.remainderkesehatanpasien.domain.usecase.GetHealthNewsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


data class NewsState(
    val articles: List<Article> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val getHealthNewsUseCase: GetHealthNewsUseCase 
) : ViewModel() {

    
    private val _state = mutableStateOf(NewsState())
    val state: State<NewsState> = _state

    private var getNewsJob: Job? = null

    private val API_KEY = "881322c0c0f64c2d812cb62c2e0d6860" 

    init {
        
        getHealthNews("technology")
    }

    fun getHealthNews(query: String) {
        getNewsJob?.cancel() 

        getNewsJob = getHealthNewsUseCase(query, API_KEY) 
            .onEach { result ->
                
                _state.value = _state.value.copy(
                    articles = result, 
                    isLoading = false,
                    error = null 
                )
            }
            .launchIn(viewModelScope) 
    }
}
