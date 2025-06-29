package com.example.remainderkesehatanpasien.broadcast

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import com.example.remainderkesehatanpasien.R
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class ReminderBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        
        val message = intent.getStringExtra("EXTRA_MESSAGE") ?: "Waktunya minum obat!"
        val description = intent.getStringExtra("EXTRA_DESC") ?: "Jangan sampai terlewat."

        // Baca pengaturan notifikasi dari DataStore secara sinkron

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Siapkan suara dering default

        val notificationBuilder = NotificationCompat.Builder(context, "reminder_channel")
            .setSmallIcon(R.drawable.notification)
            .setContentTitle(message)
            .setContentText(description)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
    }
}