package com.example.remainderkesehatanpasien.screen

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.DatePicker
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.remainderkesehatanpasien.R
import com.example.remainderkesehatanpasien.presentation.reminder.AddEditReminderEvent
import com.example.remainderkesehatanpasien.presentation.reminder.AddEditReminderViewModel
import com.example.remainderkesehatanpasien.presentation.reminder.reminderUIEvent
import kotlinx.coroutines.flow.collectLatest
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditReminderScreen(
    navController: NavController,
    viewModel: AddEditReminderViewModel = hiltViewModel()
) {
    val state by viewModel.state
    val context = LocalContext.current

    // Listener untuk event dari ViewModel (misal: simpan sukses)
    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when(event) {
                is reminderUIEvent.SaveSuccess -> {
                    navController.popBackStack() // Kembali ke layar list setelah sukses
                }
                is reminderUIEvent.ShowSnackbar -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // --- Logika untuk Date dan Time Picker ---
    val calendar = Calendar.getInstance()
    // State untuk memegang tanggal yang dipilih dari kalender
    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            // Bulan di DatePickerDialog dimulai dari 0, jadi perlu +1
            viewModel.onEvent(AddEditReminderEvent.OnDateChange(LocalDate.of(year, month + 1, dayOfMonth)))
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    // State untuk memegang waktu yang dipilih dari jam
    val timePickerDialog = TimePickerDialog(
        context,
        { _, hourOfDay: Int, minute: Int ->
            viewModel.onEvent(AddEditReminderEvent.OnTimeChange(LocalTime.of(hourOfDay, minute)))
        },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        true // 24-hour format
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tambah Pengingat") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.onEvent(AddEditReminderEvent.OnSaveReminder) }) {
                Icon(Icons.Default.Done, contentDescription = "Simpan")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = state.title,
                onValueChange = { viewModel.onEvent(AddEditReminderEvent.OnTitleChange(it)) },
                label = { Text("Judul Pengingat") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = state.description,
                onValueChange = { viewModel.onEvent(AddEditReminderEvent.OnDescriptionChange(it)) },
                label = { Text("Deskripsi (opsional)") },
                modifier = Modifier.fillMaxWidth()
            )

            Divider()

            // Baris untuk memilih tanggal
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { datePickerDialog.show() }
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.DateRange, contentDescription = "Tanggal")
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = "Tanggal: ${state.reminderDate.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))}")
            }

            // Baris untuk memilih waktu
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { timePickerDialog.show() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(painter = painterResource(id = R.drawable.time), contentDescription = "Waktu") // Asumsi Anda punya ikon waktu
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = "Waktu: ${state.reminderTime.format(DateTimeFormatter.ofPattern("HH:mm"))}")
            }

            // Baris untuk Switch "Sepanjang Hari"
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Ingatkan Sepanjang Hari")
                Switch(
                    checked = state.isAllDay,
                    onCheckedChange = { viewModel.onEvent(AddEditReminderEvent.OnAllDayToggle(it)) }
                )
            }
        }
    }
}