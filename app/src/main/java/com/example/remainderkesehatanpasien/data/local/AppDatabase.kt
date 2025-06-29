package com.example.remainderkesehatanpasien.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.remainderkesehatanpasien.data.local.entity.Note
import com.example.remainderkesehatanpasien.data.local.dao.CheckListDao
import com.example.remainderkesehatanpasien.data.local.dao.NoteDao
import com.example.remainderkesehatanpasien.data.local.dao.ReminderDao
import com.example.remainderkesehatanpasien.data.local.dao.UserDao
import com.example.remainderkesehatanpasien.data.local.entity.CheckListItem
import com.example.remainderkesehatanpasien.data.local.entity.Reminder
import com.example.remainderkesehatanpasien.data.local.entity.User

@Database(
    entities = [Note::class, User::class, CheckListItem::class, Reminder::class],
    version = 8
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
    abstract fun userDao(): UserDao
    abstract fun checkListDao(): CheckListDao
    abstract fun reminderDao(): ReminderDao

}
