package com.example.remainderkesehatanpasien.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.remainderkesehatanpasien.component.Route
import com.example.remainderkesehatanpasien.data.local.entity.Reminder
import com.example.remainderkesehatanpasien.presentation.reminder.ReminderEvent
import com.example.remainderkesehatanpasien.presentation.reminder.ReminderViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderListScreen(
    navController: NavController,
    viewModel: ReminderViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val category = viewModel.category

    Scaffold(
        topBar = {
            TopAppBar(
                title = {Text("Jadwal Pengingat")},
                navigationIcon = {
                    IconButton(onClick = {navController.popBackStack()}){
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        },

        floatingActionButton = {
            FloatingActionButton(onClick = {
                // Langsung navigasi dari sini dengan membawa kategori
                navController.navigate(
                    Route.AddEditReminder.path
                        .replace("{category}", category)
                        .replace("?reminderId={reminderId}", "")
                )
            }) {
                Icon(Icons.Default.Add, contentDescription = "Tambah Jadwal")
            }
        }
    ){ innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Pastikan pemanggilan di sini adalah state.reminders (jamak)
            items(state.reminder) { reminder ->
                ReminderItem(
                    reminder = reminder,
                    onDeleteClick = {
                        viewModel.onEvent(ReminderEvent.OnDeleteReminder(reminder))
                    }
                )
            }
        }
    }
}

@Composable
fun ReminderItem(
    reminder: Reminder,
    onDeleteClick: () -> Unit
){
    // Fungsi untuk format waktu dari Long ke String
    val dateFormat = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale.getDefault())
    val formattedTime = dateFormat.format(Date(reminder.reminderTime))

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Column(
                modifier = Modifier
                    .weight(1f)
            ){
                Text(
                    text = reminder
                        .title,
                    fontWeight = FontWeight.Bold
                )
                reminder.description?.let{
                    Text(text = it)
                }
                Spacer(
                    modifier = Modifier
                        .height(4.dp)
                )
                Text(
                    text = "Waktu: $formattedTime",
                    style = MaterialTheme.typography.bodySmall
                )
                IconButton(
                    onClick = onDeleteClick
                ){
                    Icon(Icons.Default.Delete, contentDescription = "Hapus Jadwal")
                }
            }
        }
    }
}