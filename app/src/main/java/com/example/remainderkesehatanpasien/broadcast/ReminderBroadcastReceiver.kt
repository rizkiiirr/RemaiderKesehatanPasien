package com.example.remainderkesehatanpasien.broadcast

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.remainderkesehatanpasien.R

class ReminderBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val defaultMessage = context.getString(com.example.remainderkesehatanpasien.R.string.reminder_message) // <-- GUNAKAN NAMA LENGKAP R.string
        val defaultDescription = context.getString(com.example.remainderkesehatanpasien.R.string.reminder_description) // <-- GUNAKAN NAMA LENGKAP R.string

        val message = intent.getStringExtra("EXTRA_MESSAGE") ?: defaultMessage
        val description = intent.getStringExtra("EXTRA_DESC") ?: defaultDescription
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationBuilder = NotificationCompat.Builder(context, "reminder_channel")
            .setSmallIcon(R.drawable.notification)
            .setContentTitle(message)
            .setContentText(description)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
    }
}