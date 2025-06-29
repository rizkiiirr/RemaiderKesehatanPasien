package com.example.remainderkesehatanpasien.presentation.reminder

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.remainderkesehatanpasien.data.alarm.ReminderScheduler
import com.example.remainderkesehatanpasien.data.local.entity.Reminder
import com.example.remainderkesehatanpasien.domain.usecase.AddReminderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import javax.inject.Inject

data class AddEditReminderState(
    val title: String = "",
    val description: String = "",
    val reminderDate : LocalDate = LocalDate.now(),
    val reminderTime: LocalTime = LocalTime.now().plusHours(1),
    val isAllDay: Boolean = false
    )
sealed class AddEditReminderEvent{
    data class OnTitleChange(val title: String) : AddEditReminderEvent()
    data class OnDescriptionChange(val description: String) : AddEditReminderEvent()
    data class OnDateChange(val date: LocalDate) : AddEditReminderEvent()
    data class OnTimeChange(val time: LocalTime) : AddEditReminderEvent()
    data class OnAllDayToggle(val allDay: Boolean) : AddEditReminderEvent()
    object OnSaveReminder : AddEditReminderEvent()
}

// Event dari ViewModel ke UI
sealed class reminderUIEvent{
    data class ShowSnackbar(val message: String) : reminderUIEvent()
    object SaveSuccess : reminderUIEvent()
}

@HiltViewModel
class AddEditReminderViewModel @Inject constructor(
    private val scheduler: ReminderScheduler,
    private val addReminderUseCase: AddReminderUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state = mutableStateOf(AddEditReminderState())
    val state: State<AddEditReminderState> = _state

    private val _eventFlow = MutableSharedFlow<reminderUIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val reminderCategory: String = savedStateHandle.get<String>("category") ?: "UMUM"

    fun onEvent(event: AddEditReminderEvent){
        when(event){
            is AddEditReminderEvent.OnTitleChange -> _state.value = state.value.copy(title = event.title)
            is AddEditReminderEvent.OnDescriptionChange -> _state.value = state.value.copy(description = event.description)
            is AddEditReminderEvent.OnDateChange -> _state.value = state.value.copy(reminderDate = event.date)
            is AddEditReminderEvent.OnTimeChange -> _state.value = state.value.copy(reminderTime = event.time)
            is AddEditReminderEvent.OnAllDayToggle -> _state.value = state.value.copy(isAllDay = event.allDay)
            is AddEditReminderEvent.OnSaveReminder -> {
                viewModelScope.launch {
                    try {
                        // Gabungkan tanggal dan waktu menjadi satu nilai Long (milidetik)
                        val localDateTime = LocalDateTime.of(state.value.reminderDate, state.value.reminderTime)
                        val instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant()
                        val timeInMillis = instant.toEpochMilli()

                        val newReminder = Reminder(
                            title = state.value.title,
                            description = state.value.description,
                            reminderTime = timeInMillis,
                            allDay = state.value.isAllDay,
                            category = reminderCategory
                        )
                        addReminderUseCase(newReminder)
                        scheduler.schedule(newReminder)
                        _eventFlow.emit(reminderUIEvent.SaveSuccess)
                    } catch (e: Exception) {
                        _eventFlow.emit(
                            reminderUIEvent.ShowSnackbar(
                                e.message ?: "Gagal menyimpan"
                            )
                        )
                    }
                }
            }
        }
    }
}