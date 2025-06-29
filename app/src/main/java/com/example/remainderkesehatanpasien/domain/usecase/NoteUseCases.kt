package com.example.remainderkesehatanpasien.domain.usecase

// Pastikan import ini ada jika nama file use case berbeda
import com.example.remainderkesehatanpasien.domain.usecase.DeleteNoteUseCase
import com.example.remainderkesehatanpasien.domain.usecase.GetNoteUseCase

data class NoteUseCases(
    val getNotes: GetNotesUseCase,
    val deleteNote: DeleteNoteUseCase,
    val addNote: AddNoteUseCase,
    val getNote: GetNoteUseCase
)