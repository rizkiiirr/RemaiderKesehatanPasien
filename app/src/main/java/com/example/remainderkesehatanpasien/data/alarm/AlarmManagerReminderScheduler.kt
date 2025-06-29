package com.example.remainderkesehatanpasien.data.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.remainderkesehatanpasien.broadcast.ReminderBroadcastReceiver
import com.example.remainderkesehatanpasien.data.local.entity.Reminder
import javax.inject.Inject

class AlarmManagerReminderScheduler @Inject constructor(
    private val context: Context
) : ReminderScheduler {
    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    override fun schedule(reminder: Reminder) {
        val intent = Intent(context, ReminderBroadcastReceiver::class.java).apply {
            putExtra("EXTRA_MESSAGE", reminder.title)
            putExtra("EXTRA_DESC", reminder.description)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            reminder.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // --- PERBAIKAN DI SINI ---
        // Cek apakah aplikasi memiliki izin untuk menyetel alarm tepat waktu
        // Pengecekan ini hanya relevan untuk Android 12 (SDK 31) ke atas
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    reminder.reminderTime,
                    pendingIntent
                )
            } else {
                // Jika tidak ada izin, Anda bisa arahkan pengguna ke Settings,
                // atau setel alarm yang tidak tepat waktu sebagai fallback.
                // Untuk saat ini, kita bisa abaikan jika tidak ada izin.
                // Dalam aplikasi nyata, berikan pesan ke pengguna.
            }
        } else {
            // Untuk versi Android di bawah 12, kita bisa langsung setel alarm
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                reminder.reminderTime,
                pendingIntent
            )
        }
    }


    override fun cancel(reminder: Reminder) {
        val intent = Intent(context, ReminderBroadcastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            reminder.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }
}