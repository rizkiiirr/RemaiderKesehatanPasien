package com.example.remainderkesehatanpasien.presentation.note

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.remainderkesehatanpasien.data.local.entity.Note
import com.example.remainderkesehatanpasien.domain.usecase.NoteUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


data class NoteListState(val notes: List<Note> = emptyList())
data class NoteTextFieldState(val text: String = "", val hint: String = "")

sealed class NoteEvent {
    data class EnteredTitle(val value: String) : NoteEvent()
    data class EnteredDescription(val value: String) : NoteEvent()
    data class DeleteNote(val note: Note): NoteEvent()
    object SaveNote : NoteEvent()
}

sealed class UiEvent {
    data class ShowSnackbar(val message: String) : UiEvent()
    object SaveNote : UiEvent()
}

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _listState = MutableStateFlow(NoteListState())
    val listState: StateFlow<NoteListState> = _listState.asStateFlow()

    private val _noteTitle = mutableStateOf(NoteTextFieldState(hint = "Masukkan Judul..."))
    val noteTitle: State<NoteTextFieldState> = _noteTitle

    private val _noteDescription = mutableStateOf(NoteTextFieldState(hint = "Tambahkan Catatan Anda..."))
    val noteDescription: State<NoteTextFieldState> = _noteDescription

    private var currentNoteId: Int? = null

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        getNotes()
        savedStateHandle.get<Int>("noteId")?.let { noteId ->
            if (noteId != 0) { 
                viewModelScope.launch {
                    noteUseCases.getNote(noteId)?.also { note ->
                        currentNoteId = note.id
                        _noteTitle.value = noteTitle.value.copy(text = note.title)
                        _noteDescription.value = noteDescription.value.copy(text = note.description)
                    }
                }
            }
        }
    }

    private fun getNotes() {
        noteUseCases.getNotes().onEach { notes ->
            _listState.value = listState.value.copy(notes = notes)
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: NoteEvent) {
        when (event) {
            is NoteEvent.EnteredTitle -> _noteTitle.value = noteTitle.value.copy(text = event.value)
            is NoteEvent.EnteredDescription -> _noteDescription.value = noteDescription.value.copy(text = event.value)
            is NoteEvent.SaveNote -> viewModelScope.launch {
                try {
                    noteUseCases.addNote(
                        Note(
                            id = currentNoteId ?: 0,
                            title = noteTitle.value.text,
                            description = noteDescription.value.text,
                            timestamp = System.currentTimeMillis()
                        )
                    )
                    _eventFlow.emit(UiEvent.SaveNote)
                } catch (e: Exception) {
                    _eventFlow.emit(UiEvent.ShowSnackbar(message = e.message ?: "Error"))
                }
            }
            is NoteEvent.DeleteNote -> viewModelScope.launch {
                noteUseCases.deleteNote(event.note)
            }
        }
    }
}