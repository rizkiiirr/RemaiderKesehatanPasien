package com.example.remainderkesehatanpasien.presentation.reminder

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.remainderkesehatanpasien.data.alarm.ReminderScheduler
import com.example.remainderkesehatanpasien.data.local.entity.Reminder
import com.example.remainderkesehatanpasien.domain.usecase.AddReminderUseCase
import com.example.remainderkesehatanpasien.domain.usecase.DeleteReminderUseCase
import com.example.remainderkesehatanpasien.domain.usecase.GetRemindersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


data class ReminderListState(
    val reminder: List<Reminder> = emptyList()
)


sealed class ReminderEvent{
    data class OnDeleteReminder(val reminder: Reminder) : ReminderEvent()
    
}

@HiltViewModel
class ReminderViewModel @Inject constructor(
    private val getRemindersUseCase: GetRemindersUseCase,
    private val deleteReminderUseCase: DeleteReminderUseCase,
    savedStateHandle: SavedStateHandle
    ) : ViewModel(){

    private val _state = MutableStateFlow(ReminderListState())
    val state: StateFlow<ReminderListState> = _state.asStateFlow()

    val category: String = savedStateHandle.get<String>("category") ?: "UMUM"

    private val _showDeleteDialog = mutableStateOf(false)
    val showDeleteDialog: State<Boolean> = _showDeleteDialog

    private val _reminderToDelete = mutableStateOf<Reminder?>(null)
    val reminderToDelete: State<Reminder?> = _reminderToDelete

    init {
        getRemindersUseCase(category).onEach { reminderList ->
            _state.value = state.value.copy(reminder = reminderList)
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: ReminderEvent) {
        when (event) {
            is ReminderEvent.OnDeleteReminder -> {
                viewModelScope.launch {
                    deleteReminderUseCase(event.reminder)
                }
            }
        }
    }

    fun onOpenDeleteDialog(reminder: Reminder) {
        _reminderToDelete.value = reminder
        _showDeleteDialog.value = true
    }

    
    fun onCloseDeleteDialog() {
        _reminderToDelete.value = null
        _showDeleteDialog.value = false
    }

    
    fun onConfirmDelete() {
        viewModelScope.launch {
            _reminderToDelete.value?.let { reminder ->
                deleteReminderUseCase(reminder)
            }
            onCloseDeleteDialog() 
        }
    }
}
