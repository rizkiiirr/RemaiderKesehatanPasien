package com.example.remainderkesehatanpasien.screen

import androidx.lifecycle.ViewModel
import com.example.remainderkesehatanpasien.data.Note
import com.example.remainderkesehatanpasien.data.dummyNotes

class NoteViewModel: ViewModel(){
    private val notes = ArrayList<Note>(dummyNotes())
    val note = notes.toList()
}