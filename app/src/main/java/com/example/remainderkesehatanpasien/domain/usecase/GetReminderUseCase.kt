package com.example.remainderkesehatanpasien.domain.usecase

import com.example.remainderkesehatanpasien.data.local.entity.Reminder
import com.example.remainderkesehatanpasien.domain.repository.ReminderRepository
import javax.inject.Inject

class GetReminderUseCase @Inject constructor(
    private val repository: ReminderRepository
) {
    suspend operator fun invoke(id: Int): Reminder? {
        // Tugasnya hanya mengambil satu pengingat berdasarkan ID-nya
        // (Kita perlu menambahkan fungsi ini ke repository)
        return repository.getReminderById(id)
    }
}