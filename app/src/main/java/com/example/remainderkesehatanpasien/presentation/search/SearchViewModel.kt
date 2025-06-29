package com.example.remainderkesehatanpasien.presentation.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.remainderkesehatanpasien.domain.model.SearchableItem
import com.example.remainderkesehatanpasien.domain.usecase.SearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

sealed class SearchEvent {
    data class OnQueryChange(val query: String) : SearchEvent()
}

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchUseCase: SearchUseCase
) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()
    val searchResults = searchQuery
        .flatMapLatest { query ->
            searchUseCase(query)
                .onEach { results ->
                    Log.d("SearchViewModel", "Received results in ViewModel: ${results.size} items for query '$query'")
                }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList<SearchableItem>()
        )

    fun onEvent(event: SearchEvent){
        when(event){
            is SearchEvent.OnQueryChange -> {
                _searchQuery.value = event.query
            }
        }
    }
}