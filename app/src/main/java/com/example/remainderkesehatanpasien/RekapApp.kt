package com.example.remainderkesehatanpasien

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import dagger.hilt.android.HiltAndroidApp

// Anotasi @HiltAndroidApp memberi tahu Hilt untuk menghasilkan komponen Hilt untuk aplikasi ini.
@HiltAndroidApp
class RekapApp : Application() {
    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "reminder_channel",
                "Pengingat",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.description = "Channel untuk semua notifikasi pengingat."

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}