package com.example.remainderkesehatanpasien.presentation.checkList

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.remainderkesehatanpasien.data.local.entity.CheckListItem
import com.example.remainderkesehatanpasien.domain.usecase.CheckListUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CheckListState(
    val items: List<CheckListItem> = emptyList()
)


sealed class CheckListEvent{
    data class OnItemCheckedChange(val item: CheckListItem) : CheckListEvent()
    data class OnDeleteItem(val item: CheckListItem) : CheckListEvent()
    data class OnSaveEdit(val item: CheckListItem, val newTitle: String) : CheckListEvent()
    data class OnNewItemTitleChange(val title: String) : CheckListEvent()
    object OnAddItem : CheckListEvent()
}



sealed class UIEvent{
    data class ShowSnackbar(val message: String): UIEvent()
}

@HiltViewModel
class CheckListViewModel @Inject constructor(
    private val checkListUseCases: CheckListUseCases
) : ViewModel(){

    private val _state = mutableStateOf(CheckListState())
    val state: State<CheckListState> = _state

    
    private val _newItemTitle = mutableStateOf("")
    val newItemTitle: State<String> = _newItemTitle

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init{
        
        getItems()
    }

    private fun getItems() {
        checkListUseCases.getItems()
            .onEach { items ->
                _state.value = state.value.copy(
                    items = items
                )
            }.launchIn(viewModelScope)
    }

    
    fun onEvent(event: CheckListEvent){
        when (event){
            is CheckListEvent.OnItemCheckedChange -> {
                viewModelScope.launch{
                    val updatedItem = event.item.copy(isChecked = !event.item.isChecked)
                    checkListUseCases.updateItem(updatedItem)
                }
            }

            is CheckListEvent.OnAddItem -> {
                viewModelScope.launch{
                    try{
                        checkListUseCases.addItem(newItemTitle.value)
                        
                        _newItemTitle.value = ""
                    } catch (e: Exception){
                        _eventFlow.emit(UIEvent.ShowSnackbar(e.message ?: "Input tidak valid"))
                    }
                }
            }

            is CheckListEvent.OnSaveEdit -> {
                viewModelScope.launch {
                    val updatedItem = event.item.copy(title = event.newTitle)
                    checkListUseCases.updateItem(updatedItem)
                }
            }

            is CheckListEvent.OnNewItemTitleChange -> {
                _newItemTitle.value = event.title
            }

            is CheckListEvent.OnDeleteItem -> {
                viewModelScope.launch {
                    checkListUseCases.deleteItem(event.item)
                }
            }
        }
    }
}