package com.example.remainderkesehatanpasien.domain.model

import java.sql.Timestamp

enum class ItemType{
    NOTE, CHECKLIST, REMINDER
}

class SearchableItem (
    val universalId: String,
    val originalId: Int,
    val title: String,
    val description: String,
    val timestamp: Long,
    val type: ItemType
)