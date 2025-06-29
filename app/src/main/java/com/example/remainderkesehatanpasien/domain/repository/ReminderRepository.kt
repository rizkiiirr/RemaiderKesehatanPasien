package com.example.remainderkesehatanpasien.domain.repository

import com.example.remainderkesehatanpasien.data.local.entity.Reminder
import kotlinx.coroutines.flow.Flow

interface ReminderRepository {
    fun getReminders(category: String): Flow<List<Reminder>>
    fun getAllReminders(): Flow<List<Reminder>>
    suspend fun getReminderById(id: Int): Reminder? // <-- TAMBAHKAN INI
    suspend fun insertReminder(reminder: Reminder)
    suspend fun deleteReminder(reminder: Reminder)
}