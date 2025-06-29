package com.example.remainderkesehatanpasien.data.repository

import com.example.remainderkesehatanpasien.data.local.dao.ReminderDao
import com.example.remainderkesehatanpasien.data.local.entity.Reminder
import com.example.remainderkesehatanpasien.domain.repository.ReminderRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReminderRepositoryImpl @Inject constructor(
    private val dao: ReminderDao
) : ReminderRepository{

    override fun getReminders(category: String): Flow<List<Reminder>> {
        return dao.getRemindersByCategory(category)
    }

    override fun getAllReminders(): Flow<List<Reminder>> {
        return dao.getAllReminders()
    }

    override suspend fun insertReminder(reminder: Reminder){
        dao.insertReminder(reminder)
    }

    override suspend fun deleteReminder(reminder: Reminder){
        dao.deleteReminder(reminder)
    }
}