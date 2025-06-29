package com.example.remainderkesehatanpasien.domain.usecase

import com.example.remainderkesehatanpasien.data.local.entity.Note
import com.example.remainderkesehatanpasien.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

// Use case ini hanya memiliki satu tugas: mendapatkan notes.
// Dia membutuhkan sebuah implementasi dari NoteRepository untuk bekerja.
class GetNotesUseCase(
    private val repository: NoteRepository
) {
    // 'operator fun invoke' memungkinkan kita memanggil class ini seolah-olah sebuah fungsi
    // contoh: getNotesUseCase()
    operator fun invoke(): Flow<List<Note>> {
        return repository.getNotes()
    }
}