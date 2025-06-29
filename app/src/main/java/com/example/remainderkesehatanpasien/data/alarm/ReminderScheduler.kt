package com.example.remainderkesehatanpasien.data.alarm

import com.example.remainderkesehatanpasien.data.local.entity.Reminder

interface ReminderScheduler {
    fun schedule(reminder: Reminder)
    fun cancel(reminder: Reminder)
}