package com.example.remainderkesehatanpasien.domain.usecase

import com.example.remainderkesehatanpasien.data.local.entity.Note
import com.example.remainderkesehatanpasien.domain.repository.NoteRepository

class DeleteNoteUseCase(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(note: Note) {
        repository.deleteNote(note)
    }
}