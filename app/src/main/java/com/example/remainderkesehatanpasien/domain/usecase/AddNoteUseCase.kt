package com.example.remainderkesehatanpasien.domain.usecase

import com.example.remainderkesehatanpasien.data.local.entity.Note
import com.example.remainderkesehatanpasien.domain.InvalidNoteException
import com.example.remainderkesehatanpasien.domain.repository.NoteRepository

class AddNoteUseCase(
    private val repository: NoteRepository
){
    @Throws(InvalidNoteException::class)
    suspend operator fun invoke(note: Note){
        if (note.title.isBlank()){
            throw InvalidNoteException("Judul Tidak Boleh Kosong!")
        }
        repository.insertNote(note)
    }
}