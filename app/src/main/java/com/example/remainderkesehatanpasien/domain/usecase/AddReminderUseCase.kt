package com.example.remainderkesehatanpasien.domain.usecase

import com.example.remainderkesehatanpasien.data.local.entity.Reminder
import com.example.remainderkesehatanpasien.domain.InvalidInputException
import com.example.remainderkesehatanpasien.domain.repository.ReminderRepository
import javax.inject.Inject

class AddReminderUseCase @Inject constructor(
    private val repository: ReminderRepository
) {
    @Throws(InvalidInputException::class)
    suspend operator fun invoke(reminder: Reminder){
        if (reminder.title.isBlank()){
            throw InvalidInputException("Judul pengingat tidak boleh kosong")
        }
        if(reminder.reminderTime <= System.currentTimeMillis()){
            throw InvalidInputException("Waktu pengingat invalid")
        }
        repository.insertReminder(reminder)
    }
}