package com.example.remainderkesehatanpasien.domain.usecase

import android.util.Log
import com.example.remainderkesehatanpasien.domain.model.ItemType
import com.example.remainderkesehatanpasien.domain.model.SearchableItem
import com.example.remainderkesehatanpasien.domain.repository.CheckListRepository
import com.example.remainderkesehatanpasien.domain.repository.NoteRepository
import com.example.remainderkesehatanpasien.domain.repository.ReminderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class SearchUseCase(
    private val noteRepository: NoteRepository,
    private val checkListRepository: CheckListRepository,
    private val reminderRepository: ReminderRepository
    ) {
    operator fun invoke(query: String): Flow<List<SearchableItem>> {
        val notesFlow = noteRepository.getNotes()
        val checklistsFlow = checkListRepository.getAllItems()
        val reminderFlow = reminderRepository.getAllReminders()

        return combine(notesFlow, checklistsFlow, reminderFlow){ notes, checklists, reminders ->
            val searchableItem = notes.map{ note ->
                SearchableItem(
                    universalId = "note_${note.id}",
                    originalId = note.id,
                    title = note.title,
                    description = note.description,
                    timestamp = note.timestamp,
                    type = ItemType.NOTE
                )
            }

            val searchableCheckList = checklists.map{checklist ->
                SearchableItem(
                    universalId = "checklist_${checklist.id}",
                    originalId = checklist.id,
                    title = checklist.title,
                    description = if (checklist.isChecked) "Selesai" else "Belum Selesai",
                    timestamp = checklist.timestamp,
                    type = ItemType.CHECKLIST
                )
            }

            val searchableReminder = reminders.map{reminder ->
                SearchableItem(
                    universalId = "reminder_${reminder.id}",
                    originalId = reminder.id,
                    title = reminder.title,
                    description = reminder.description ?: "",
                    timestamp = reminder.reminderTime,
                    type = ItemType.REMINDER
                )
            }

            val allItems = (searchableItem + searchableCheckList + searchableReminder)
                .sortedByDescending { it.timestamp }

            Log.d("SearchUseCase", "Total items combined: ${allItems.size}")

            if (query.isBlank()){
                allItems
            } else{
                allItems.filter{
                    it.title.contains(query, ignoreCase = true)
                }
            }
        }
    }

}