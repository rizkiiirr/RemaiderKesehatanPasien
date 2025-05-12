package com.example.remainderkesehatanpasien.data

data class Note(
    val id: Int,
    val title: String,
    val description: String
)

fun dummyNotes(): List<Note>{
    val items = arrayListOf<Note>()

    for (index in 1..15 ){
        items.add(
            Note(index,"Judul $index", "Deskripsi $index")
        )
    }
    return items
}