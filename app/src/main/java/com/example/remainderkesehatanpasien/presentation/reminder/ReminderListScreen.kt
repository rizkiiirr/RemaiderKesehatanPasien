package com.example.remainderkesehatanpasien.presentation.reminder

import androidx.compose.foundation.clickable
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.remainderkesehatanpasien.presentation.navigation.Route
import com.example.remainderkesehatanpasien.data.local.entity.Reminder
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

    var showDeleteDialog by remember { mutableStateOf(false) }
    var reminderToDelete by remember { mutableStateOf<Reminder?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (category == "OBAT") "Jadwal Minum Obat" else "Jadwal Konsultasi") },
                colors = TopAppBarDefaults.topAppBarColors( 
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                navigationIcon = {
                    IconButton(onClick = {navController.popBackStack()}){
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        },

        floatingActionButton = {
            FloatingActionButton(onClick = {
                
                navController.navigate(
                    Route.AddEditReminder.path
                        .replace("{category}", category)
                        .replace("?reminderId={reminderId}", "")
                )
            }, containerColor = MaterialTheme.colorScheme.secondary, 
               contentColor = MaterialTheme.colorScheme.onSecondary
            ) {
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
            
            items(state.reminder) { reminder ->
                ReminderItem(
                    reminder = reminder,
                    
                    onItemClick = {
                        navController.navigate(
                            Route.AddEditReminder.path
                                .replace("{category}", reminder.category)
                                .replace("{reminderId}", reminder.id.toString())
                        )
                    },
                    
                    onDeleteClick = {
                        reminderToDelete = reminder
                        showDeleteDialog = true
                    }
                )
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(text = "Konfirmasi Hapus") },
            text = { Text(text = "Apakah Anda yakin ingin menghapus pengingat '${reminderToDelete?.title}'?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        
                        reminderToDelete?.let { viewModel.onEvent(ReminderEvent.OnDeleteReminder(it)) }
                        showDeleteDialog = false
                    }
                ) {
                    Text("Hapus")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }
}

@Composable
fun ReminderItem(
    reminder: Reminder,
    onItemClick: () -> Unit,
    onDeleteClick: () -> Unit
){
    
    val dateFormat = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale.getDefault())
    val formattedTime = dateFormat.format(Date(reminder.reminderTime))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onItemClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant) 

    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Column(
                modifier = Modifier
                    .weight(1f)
            ){
                Text(
                    text = reminder.title,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant 

                )
                reminder.description?.let{
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f) 
                    )
                }
                Spacer(
                    modifier = Modifier
                        .height(4.dp)
                )
                Text(
                    text = "Waktu: $formattedTime",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f) 

                )
            }
            
            IconButton(onClick = onDeleteClick){
                Icon(Icons.Default.Delete, contentDescription = "Hapus Jadwal")
            }
        }
    }
}