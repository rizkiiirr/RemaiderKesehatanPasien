package com.example.remainderkesehatanpasien.data.repository

import com.example.remainderkesehatanpasien.data.local.entity.Note
import com.example.remainderkesehatanpasien.data.local.dao.NoteDao
import com.example.remainderkesehatanpasien.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

// Ini adalah implementasi nyata. Dia tahu BAGAIMANA cara mendapatkan data (misal dari DAO).
class NoteRepositoryImpl(
    private val dao: NoteDao
) : NoteRepository { // Mengimplementasikan interface dari Domain

    override fun getNotes(): Flow<List<Note>> {
        return dao.getAllNotes() // Mengambil data dari database lokal
    }

    override suspend fun getNoteById(id: Int): Note? {
        return dao.getNoteById(id)
    }

    override suspend fun insertNote(note: Note) {
        dao.insertNote(note)
    }

    override suspend fun deleteNote(note: Note) {
        dao.deleteNote(note)
    }
}